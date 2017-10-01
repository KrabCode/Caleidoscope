package Drawables;

import Drawables.Abstract.Drawable;
import Sound.SoundAnalysis;
import processing.core.PApplet;
import Math.Rectangle;


public class SoundSpectrumViewer extends Drawable {
    Rectangle bounds;
    PApplet p;
    public SoundSpectrumViewer(PApplet p, Rectangle rect){

        this.p = p;
    }

    int fore = 255;

    @Override
    public void draw(SoundAnalysis sa) {
        p.stroke(fore);
        if(sa.getSpectrum() != null) {
            //scan across the pixels
            for(int x = 0; x < p.width; x++)
            {
                int spectrumIndex = (x * sa.getSpectrum().length) / p.width;
                int barHeight = Math.min((int)(sa.getSpectrum()[spectrumIndex] * p.height), p.height - 1);
                p.line(x, p.height, x, p.height - barHeight);
            }
        }
    }
}
