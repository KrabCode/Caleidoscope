package drawable.diag;

import processing.core.PApplet;

import drawable.Drawable;
import sound.Range;
import sound.SoundAnalysis;
import utils.geometry.*;

import java.util.List;


public class SoundSpectrumViewer extends Drawable {
    Rectangle bounds;
    PApplet p;
    List<Range> beingVisualised;

    int color =255;
    int pointOfInterestColorFromR = 0;
    int pointOfInterestColorFromG = 0;
    int pointOfInterestColorFromB = 255;

    int pointOfInterestColorToR = 255;
    int pointOfInterestColorToG = 0;
    int pointOfInterestColorToB = 0;



    public SoundSpectrumViewer(PApplet p, Rectangle rect, List<Range> beingVisualised){
        this.bounds = rect;
        this.p = p;
        this.beingVisualised = beingVisualised;
    }

    float getRangeStartX(int rangeIndex){
        int x = beingVisualised.get(rangeIndex).getFrom();
        return bounds.getA().x + x;
    }

    float getRangeEndX(int rangeIndex){
        int x = beingVisualised.get(rangeIndex).getTo();
        return bounds.getA().x + x;
    }

    boolean isRangeStart(int spectrumIndex){
        //isRangeStart this spectrumIndex being visualised as an interesting boundary?
        boolean rangeStart = false;
        for (Range range : beingVisualised){
            if(range.getFrom() == spectrumIndex &&  spectrumIndex != 0){
                rangeStart=true;
                break;
            }
        }
        return rangeStart;
    }

    boolean isRangeEnd(int spectrumIndex){
        //isRangeStart this spectrumIndex being visualised as an interesting boundary?
        boolean rangeEnd = false;
        for (Range range : beingVisualised){
            if(range.getTo() == spectrumIndex){
                rangeEnd=true;
                break;
            }
        }
        return rangeEnd;
    }

    @Override
    public void draw(SoundAnalysis sa) {

        if(sa.getSpectrum() != null) {
            //scan across the pixels
            for(int x = 0; x < getWidth(); x++)
            {
                int spectrumIndex = (x * sa.getSpectrum().length) / getWidth();

                if(isRangeStart(spectrumIndex)){
                    //draw first special colour line
                    p.stroke(pointOfInterestColorFromR, pointOfInterestColorFromG, pointOfInterestColorFromB, 50);
                    p.strokeWeight(1);
                    //draw a full line
                    p.line(bounds.getA().x + x,
                            bounds.getA().y,
                            bounds.getA().x + x,
                            bounds.getD().y);

                }else if (isRangeEnd(spectrumIndex)){
                    //draw second special colour line
                    p.stroke(pointOfInterestColorToR, pointOfInterestColorToG, pointOfInterestColorToB, 50);
                    p.strokeWeight(1);
                    //draw a full line
                    p.line(bounds.getA().x + x,
                            bounds.getA().y,
                            bounds.getA().x + x,
                            bounds.getD().y);

                }else {
                    //draw the standard line
                    p.stroke(color, 50);
                    p.strokeWeight(1);
                }
                int barHeight = Math.min((int)(sa.getSpectrum()[spectrumIndex] * getHeight()), getHeight() - 1);
                p.line(bounds.getA().x + x,
                        bounds.getD().y,
                        bounds.getA().x + x,
                        bounds.getD().y - barHeight);
            }
        }
    }

    int getWidth() {
        return ((int) bounds.getB().x - (int) bounds.getA().x);
    }

    int getHeight(){
        return ((int)bounds.getD().y - (int)bounds.getA().y);
    }
}
