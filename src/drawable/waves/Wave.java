package drawable.waves;
import drawable.Drawable;
import utils.geometry.*;
import sound.Range;
import sound.SoundAnalysis;
import processing.core.PApplet;

public class Wave extends Drawable {

    float r, deviation, baseR, spd,
            alpha, strokeWeight,
            spokes, angleOffset, strokePeakWeight,
            strokeR, strokeG, strokeB,
            strokePeakR, strokePeakG, strokePeakB;
    Point center;
    PApplet p;

    Range range;

    boolean odd = true;

    public Wave(PApplet p, float r, Range freqRange, float deviation){
        this.p = p;
        this.r = r;
        this.baseR = r;
        this.deviation = deviation;
        this.range = freqRange;
        spd = 0f;
        alpha = 50;

        strokeWeight = 2;
        strokeR = 255;
        strokeG = 0;
        strokeB = 0;

        strokePeakWeight = 3;
        strokePeakR = 255;
        strokePeakG = 255;
        strokePeakB = 255;

        spokes = 28; // must be even

        angleOffset = 0;

        center = new Point(p.width/2, p.height/2);
    }

    @Override
    public void draw(SoundAnalysis sa) {
        r+= spd;
        float avg = sa.getAvg(
                sa.getSpectrum(),
                getRange()
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
     * Returns a full range.
     * @param sa
     * @return
     */
    public Range getRange() {
        return range;
    }

    /**
     * Returns a small deviation.
     * This multiplies the distance of the high volume avgs from the standstill radius.
     * @return
     */
    public float getDeviation(){
        return deviation;
    }

}