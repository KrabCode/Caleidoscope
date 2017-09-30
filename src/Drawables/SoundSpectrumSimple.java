package Drawables;

import Sound.SoundAnalysis;
import processing.core.PApplet;

public class SoundSpectrumSimple extends Drawable {
    PApplet p;
    public SoundSpectrumSimple(PApplet p){
        this.p = p;
    }

    int fore = 255;

    @Override
    public void draw(SoundAnalysis sa) {
        p.stroke(fore);
        if(sa.spectrum != null) {
            //scan across the pixels
            for(int x = 0; x < p.width; x++)
            {
                int spectrumIndex = (x * sa.spectrum.length) / p.width;
                int barHeight = Math.min((int)(sa.spectrum[spectrumIndex] * p.height), p.height - 1);
                p.line(x, p.height, x, p.height - barHeight);
            }
        }
    }


}
