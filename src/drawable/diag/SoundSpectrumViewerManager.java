package drawable.diag;

import drawable.Drawable;
import drawable.Manager;
import drawable.waves.WaveManager;
import sound.Range;
import sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.List;

import utils.geometry.*;

public class SoundSpectrumViewerManager extends Manager {

    PApplet p;
    Rectangle rect;
    List<Range> rangesBeingVisualised;
    SoundSpectrumViewer soundSpectrumViewer;


    public SoundSpectrumViewerManager(PApplet p, Point start, Point end, List<Range> rangesBeingVisualised){
        this.p = p;
        this.rect = Rectangle.fromPoints(start, end);
        this.rangesBeingVisualised = rangesBeingVisualised;

    }

    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {
        //only make 1
        if(soundSpectrumViewer == null){
            soundSpectrumViewer = new SoundSpectrumViewer(p, rect, rangesBeingVisualised);
            drawables.add(soundSpectrumViewer);

            p.println("Monitoring the following frequency ranges");
            for(int i = 0; i < rangesBeingVisualised.size(); i++){
                p.println( i + ":\tfrom\t"+ rangesBeingVisualised.get(i).getFrom() + "\tto\t" + rangesBeingVisualised.get(i).getTo());
            }
        }
        return drawables;
    }

    public void handleMouseInput(WaveManager wm){
       for(int i = 0; i < soundSpectrumViewer.beingVisualised.size(); i++)
       {

       }
    }
}
