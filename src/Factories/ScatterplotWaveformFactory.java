package Factories;

import Drawables.Drawable;
import Drawables.ScatterplotWaveform;
import processing.core.PApplet;

import java.util.List;

public class ScatterplotWaveformFactory extends DrawableFactory {

    PApplet p;
    public ScatterplotWaveformFactory(PApplet p){
        this.p = p;
    }

    Drawable scatterplotWaveform;

    @Override
    public List<Drawable> update(List<Drawable> drawables, float[] spectrum) {
        if(scatterplotWaveform == null){
            scatterplotWaveform = new ScatterplotWaveform(p);
            drawables.add(scatterplotWaveform);
        }
        return drawables;
    }
}
