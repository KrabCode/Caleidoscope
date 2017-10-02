package Drawables.Utils;

import Drawables.Abstract.Drawable;
import Drawables.Abstract.Manager;
import Sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.List;
import Math.Rectangle;
import Math.Point;

public class SoundSpectrumViewerManager extends Manager {

    PApplet p;
    Rectangle rect;
    int[] pointsOfInterest;
    SoundSpectrumViewer soundSpectrumViewer;


    public SoundSpectrumViewerManager(PApplet p, Point start, Point end, int[] pointsOfInterest){
        this.p = p;
        this.rect = Rectangle.fromPoints(start, end);
        this.pointsOfInterest = pointsOfInterest;

    }

    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {
        //only make 1
        if(soundSpectrumViewer == null){
            soundSpectrumViewer = new SoundSpectrumViewer(p, rect, pointsOfInterest);
            drawables.add(soundSpectrumViewer);
        }
        return drawables;
    }


}
