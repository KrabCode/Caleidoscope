package utils;

import com.hamoid.VideoExport;
import processing.core.PApplet;

public class Recorder {

    private VideoExport vid;
    private int videoQualityPercent = 100;
    private int audioQualityKbps = 192;

    private PApplet p;

    public Recorder(PApplet p){
        this.p = p;
        vid = new VideoExport(p);
        vid.setQuality(videoQualityPercent, audioQualityKbps);
        vid.startMovie();
    }

    public void saveFrame() {
        vid.saveFrame();
    }

    public void endRecording(){
        vid.endMovie();
    }
}
