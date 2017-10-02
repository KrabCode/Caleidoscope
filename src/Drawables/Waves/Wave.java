package Drawables.Waves;

import Drawables.Abstract.Drawable;
import Math.Angle;
import Math.Point;
import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class Wave extends Drawable {

    float r, baseR, spd, alpha, strokeWeight, spokes, angleOffset, strokePeakWeight,
    strokeR, strokeG, strokeB,strokePeakR, strokePeakG, strokePeakB;
    Point center;
    PApplet p;

    boolean odd = true;

    public Wave(PApplet p, float r){
        this.p = p;
        this.r = r;
        this.baseR = r;

        spd = 0f;
        alpha = 30;

        strokeWeight = 1;
        strokeR = 255;
        strokeG = 0;
        strokeB = 0;

        strokePeakWeight = 2;
        strokePeakR = 255;
        strokePeakG = 255;
        strokePeakB = 255;

        spokes = 128;

        angleOffset = 0;

        center = new Point(p.width/2, p.height/2);
    }

    @Override
    public void draw(SoundAnalysis sa) {
        r+= spd;
        float avg = sa.getAvg(
                sa.getSpectrum(),
                getRange(sa.getSpectrum().length)
        );
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

            //SPOKES OF A WHEEL
            if(i%2==0){
                target = Angle.getPointAtAngle(center, r + avg*getDeviation(), angle );
            }else{
                target = Angle.getPointAtAngle(center, r - avg*getDeviation(), angle );
            }


            //VIBRATING STRINGS
//            if(odd){
//                if(i%2==0){
//                    target = Angle.getPointAtAngle(center, r + avg*deviation, angle );
//                }else{
//                    target = Angle.getPointAtAngle(center, r - avg*deviation, angle );
//                }
//            }else{
//                if(i%2!=0){ //only difference
//                    target = Angle.getPointAtAngle(center, r + avg*deviation, angle );
//                }else{
//                    target = Angle.getPointAtAngle(center, r - avg*deviation, angle );
//                }
//            }

            odd = !odd;

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
    public Range getRange(int spectrumLength) {
        return new Range(0,spectrumLength);
    }

    /**
     * Returns a small deviation.
     * This multiplies the distance of the high volume avgs from the standstill radius.
     * Override to change deviation intensity.
     * @return
     */
    public float getDeviation(){
        return 1;
    }
}