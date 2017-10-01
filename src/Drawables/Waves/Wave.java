package Drawables.Waves;

import Drawables.Abstract.Drawable;
import Math.Angle;
import Math.Point;
import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class Wave extends Drawable {

    float r, baseR, spd, deviation, alpha, strokeWeight, spokes, angleOffset, strokePeakWeight,
    strokeR, strokeG, strokeB,strokePeakR, strokePeakG, strokePeakB;
    Point center;
    PApplet p;

    public Wave(PApplet p, float r, float deviation){
        this.p = p;
        this.r = r;
        this.baseR = r;
        this.deviation = deviation;
        spd = 0f;
        alpha = 80;

        strokeWeight = 2;

        strokeR = 255;
        strokeG = 0;
        strokeB = 0;

        strokePeakWeight = 3;
        strokePeakR = 255;
        strokePeakG = 255;
        strokePeakB = 255;

        spokes = 20;

        angleOffset = 0;

        center = new Point(p.width/2, p.height/2);
    }

    @Override
    public void draw(SoundAnalysis sa) {
        r+= spd;
        float avg = sa.getAvg(sa.getSpectrum(), getRange(sa));
        Point lastTarget = null;

        //React to peaks
        if(sa.getPeak()){
            p.stroke(strokePeakR, strokePeakG, strokePeakB, alpha);
            p.strokeWeight(strokePeakWeight);
        }else{
            p.stroke(strokeR, strokeG, strokeB, alpha);
            p.strokeWeight(strokeWeight);
            if(r>baseR){

            }
        }

        float oneStep = 360f/ spokes;
        for(int i = 0; i < spokes + 1 ; i++){
            float angle = i*oneStep + angleOffset;
            Point target;
            if(i%2==0){
                target = Angle.getPointAtAngle(center, r + avg*deviation, angle );
            }else{
                target = Angle.getPointAtAngle(center, r - avg*deviation, angle );
            }

            if(lastTarget != null) // the reason for the "+1" in the loop count - the first loop doesn't get drawn
            {
                p.line(target.x, target.y, lastTarget.x, lastTarget.y);
            }
            lastTarget = target;
        }
    }

    /**
     * Returns a full range. Override this to change what frequencies this wave reacts to.
     * @param sa
     * @return
     */
    public Range getRange(SoundAnalysis sa) {
        return new Range(0,sa.getSpectrum().length);
    }

}