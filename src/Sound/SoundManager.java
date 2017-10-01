package Sound;

import beads.*;

import java.io.File;

public class SoundManager {
    private AudioContext ac;
    private PowerSpectrum ps;
    private PeakDetector pd;

    private String soundFilepath = "";
    //TODO private String soundFolderpath = "";

    private float pdThreshold = 0.1f;
    private boolean peak = false;

    public SoundManager (String soundFilepath, boolean playSong, float pdThreshold){
        this.soundFilepath = soundFilepath;
        this.pdThreshold = pdThreshold;
        setupAudio(playSong);
    }

    private void setupAudio(boolean playSong) {
        ac = new AudioContext();
        if(playSong){
            //Higher quality analysis
            //sets the visualiser to listen to the song being played as well as play it
            File soundFile = new File(soundFilepath);
            String audioFileName = soundFile.getAbsolutePath();
            SamplePlayer player = new SamplePlayer(ac, SampleManager.sample(audioFileName));

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
}
