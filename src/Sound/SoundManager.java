package Sound;

import Drawables.Abstract.Drawable;
import Managers.Abstract.Manager;
import beads.*;
import processing.core.PApplet;

import java.io.File;
import java.util.List;

public class SoundManager extends Manager{
    private AudioContext ac;
    private PowerSpectrum ps;
    private PeakDetector pd;
    private PApplet p;

    private List<File> playlistQueue;
    private boolean initFlag = true;

    SamplePlayer player;

    private float pdThreshold = 0.1f;
    private boolean peak = false;

    public SoundManager (PApplet p, List<File> playlist, boolean playSong, float pdThreshold){
        this.p = p;
        this.playlistQueue = playlist;
        this.pdThreshold = pdThreshold;
        setupAudio(playSong);
    }

    private void setupAudio(boolean playSong) {
        ac = new AudioContext();
        if(playSong){
            //Higher quality analysis
            //sets the visualiser to listen to the song being played as well as play it
            tryPlayNext();
            Gain g = new Gain(ac, 2, 0.2f);
            g.addInput(player);
            ac.out.addInput(g);
        }else{
            //Lower quality analysis
            //sets the visualiser to listen to the default microphone (hopefully Stereo Mix)
            Gain g = new Gain(ac, 2, 0.6f);
            UGen microphoneIn = ac.getAudioInput();
            g.addInput(microphoneIn);
            ac.out.addInput(g);
        }


        //setup visualiser
        ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
        sfs.addInput(ac.out);
        FFT fft = new FFT();
        ps = new PowerSpectrum();
        sfs.addListener(fft);
        fft.addListener(ps);
        ac.out.addDependent(sfs);

        //setup beat detector
        SpectralDifference sd = new SpectralDifference(ac.getSampleRate());
        ps.addListener(sd);
        pd = new PeakDetector();
        sd.addListener(pd);

        // the threshold is the gain level that will trigger the
        // beat detector - this will vary on each recording

        pd.setThreshold(pdThreshold);
        pd.setAlpha(.9f);
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
}
