package Drawables.Utils;

import Drawables.Abstract.Drawable;
import Sound.SoundAnalysis;
import processing.core.PApplet;
import Math.Rectangle;


public class SoundSpectrumViewer extends Drawable {
    Rectangle bounds;
    PApplet p;
    int[] pointsOfInterest;

    public SoundSpectrumViewer(PApplet p, Rectangle rect, int[] pointsOfInterest){
        this.bounds = rect;
        this.p = p;
        this.pointsOfInterest = pointsOfInterest;
    }

    int color =255;
    int pointOfInterestColorR = 255;
    int pointOfInterestColorG = 0;
    int pointOfInterestColorB = 0;

    @Override
    public void draw(SoundAnalysis sa) {

        if(sa.getSpectrum() != null) {
            //scan across the pixels
            for(int x = 0; x < getWidth(); x++)
            {
                //draw the standard line
                int spectrumIndex = (x * sa.getSpectrum().length) / getWidth();

                //get the color
                boolean isPointOfInterest = false;
                for (float pointOfInterest : pointsOfInterest){
                    if(pointOfInterest == spectrumIndex){
                        isPointOfInterest=true;
                        break;
                    }
                }
                if(isPointOfInterest){
                    p.stroke(pointOfInterestColorR, pointOfInterestColorG, pointOfInterestColorB, 50);
                    p.strokeWeight(1);
                    //draw a full line
                    p.line(bounds.getA().x + x,
                            bounds.getA().y,
                            bounds.getA().x + x,
                            bounds.getD().y);
                }else {
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
