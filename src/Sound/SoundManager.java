package Sound;

import Drawables.Abstract.Drawable;
import Drawables.Abstract.Manager;
import beads.*;
import processing.core.PApplet;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoundManager extends Manager{
    private AudioContext ac;
    private PowerSpectrum ps;
    private PeakDetector pd;
    private PApplet p;
    private boolean playback = true;
    private List<File> playlistQueue;
    private boolean initFlag = true;

    SamplePlayer player;

    private float pdThreshold = 0.1f;
    private boolean peak = false;

    private final JFileChooser fc = new JFileChooser();



    public SoundManager (PApplet p, List<File> playlist, float pdThreshold, String defaultDir){
        this.p = p;
        this.playlistQueue = playlist;
        this.pdThreshold = pdThreshold;
        this.playback = true; // assuming true until disproven by the open file dialog
        this.playlistQueue = promptUserForSoundtrack(defaultDir);
        setupAudio(playback);
    }

    private void setupAudio(boolean playback) {
        ac = new AudioContext();

        if(playback){
            //Higher quality analysis
            //sets the visualiser to listen to the song being played as well as play it
            tryPlayNext();
            Gain g = new Gain(ac, 2, 0.2f);
            g.addInput(player);
            ac.out.addInput(g);
        }else{
            //Lower quality analysis
            //sets the visualiser to listen to the default microphone (hopefully Stereo Mix)
            Gain g = new Gain(ac, 2, 0.1f);
            UGen microphoneIn = ac.getAudioInput(new int[]{});
            g.addInput(microphoneIn);
            ac.out.addInput(g);
        }


        //setup fast fourier transform -> the main visualiser input
        ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
        sfs.addInput(ac.out);
        FFT fft = new FFT();
        ps = new PowerSpectrum();
        sfs.addListener(fft);
        fft.addListener(ps);
        ac.out.addDependent(sfs);

        //setup beat detector -> secondary visualiser input
        SpectralDifference sd = new SpectralDifference(ac.getSampleRate());
        ps.addListener(sd);
        pd = new PeakDetector();
        sd.addListener(pd);

        // the threshold is the gain level that will trigger the
        // beat detector - this will vary on each recording

        pd.setThreshold(pdThreshold);
        pd.setAlpha(.2f);
        // whenever our beat detector finds a beat, set a global
        // variable
        pd.addMessageListener
                (
                        new Bead()
                        {
                            protected void messageReceived(Bead b)
                            {
                                peak = true;
                            }
                        }
                );
        ac.start();
    }

    public SoundAnalysis getFreshAnalysis() {
        SoundAnalysis newAnalysis = new SoundAnalysis();
        newAnalysis.spectrum = ps.getFeatures();
        newAnalysis.peak = peak;        //pass the current value
        peak = false;                   //reset the peak
        return newAnalysis;
    }

    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {
        if(!noSongPlaying()){
            tryPlayNext();
        }
        return drawables;
    }

    public void tryPlayNext(){
        if(!initFlag){
            playlistQueue.remove(0);
        }
        try{
            playSong(playlistQueue.get(0).getAbsolutePath());
        }catch (Exception ex){
            ex.printStackTrace();
            if(playlistQueue.size()>0){
                tryPlayNext();
            }
        }
        initFlag = false;
    }

    private void playSong(String soundFilepath){
        File song = new File(soundFilepath);
        if(isSongPlayable(song)){
            String path = song.getAbsolutePath();
            if(player == null){
                player = new SamplePlayer(ac, SampleManager.sample(path));
            }else{
                player.setSample(SampleManager.sample(path));
            }
            player.setKillOnEnd(true);
        }
    }

    private boolean isSongPlayable(File song){
        if(song.getAbsolutePath().contains(".mp3")){
            return true;
        }
        return false;
    }

    private boolean noSongPlaying(){
        return player == null || player.isDeleted();
    }

    public List<File> promptUserForSoundtrack(String defaultDir) {

        List<File> songFiles = new ArrayList<File>();
        fc.setDialogTitle("Choose one or more .mp3 files");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        fc.setCurrentDirectory(new File(defaultDir));
        fc.setApproveButtonText("Play");
        fc.setBounds(200,200,400,800);
        int returnVal = fc.showOpenDialog(p.frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selection = (fc.getSelectedFiles());
            int i = 0;
            for (File file : selection) {
                songFiles.add(file);
                p.println(i++ + ": " + file.getName());
            }
            if(songFiles.isEmpty()){
                promptUserForSoundtrack(defaultDir);
                //recursive nagging - gotta be a bit relentless
                //user clicked on APPROVE_OPTION and there's no files selected
                //like wtf dude, shape up or ship out
            }
        }else{
            playback = false;
        }
        return songFiles;
    }
}
