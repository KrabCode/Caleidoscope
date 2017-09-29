package Drawables;

import processing.core.PApplet;

public class ScatterplotWaveform extends Drawable {
    PApplet p;
    public ScatterplotWaveform(PApplet p){
        this.p = p;
    }

    int fore = 255;

    @Override
    public void draw(float[] spectrum) {
        p.stroke(fore);
        if(spectrum != null) {
            //scan across the pixels
            for(int i = 0; i < p.width; i++) {
                int featureIndex = i * spectrum.length / p.width;
                int vOffset = p.height - 1 - Math.min((int)(spectrum[featureIndex] * p.height), p.height - 1);
                p.line(i,p.height,i,vOffset);
            }
        }
    }
}
