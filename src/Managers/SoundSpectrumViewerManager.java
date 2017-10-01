package Managers;

import Drawables.Abstract.Drawable;
import Drawables.SoundSpectrumViewer;
import Managers.Abstract.Manager;
import Sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.List;
import Math.Rectangle;

public class SoundSpectrumViewerManager extends Manager {

    PApplet p;
    Rectangle rect;

    public SoundSpectrumViewerManager(PApplet p, Rectangle rect){
        this.p = p;
    }

    Drawable scatterplotWaveform;

    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {
        //only make 1
        if(scatterplotWaveform == null){
            scatterplotWaveform = new SoundSpectrumViewer(p, rect);
            drawables.add(scatterplotWaveform);
        }
        return drawables;
    }


}
