package Factories;

import Drawables.Drawable;
import Drawables.SoundSpectrumSimple;
import Sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.List;

public class SoundSpectrumSimpleFactory extends DrawableFactory {

    PApplet p;
    public SoundSpectrumSimpleFactory(PApplet p){
        this.p = p;
    }

    Drawable scatterplotWaveform;

    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {
        //only make 1
        if(scatterplotWaveform == null){
            scatterplotWaveform = new SoundSpectrumSimple(p);
            drawables.add(scatterplotWaveform);
        }
        return drawables;
    }


}
