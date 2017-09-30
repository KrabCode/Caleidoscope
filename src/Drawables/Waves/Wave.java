package Drawables.Waves;

import Drawables.Drawable;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class Wave extends Drawable {

    float r, baseR, spd, deviation, alpha, strokeWeight, resolution, angleOffset;
    Point center;
    PApplet p;


    public Wave(PApplet p, float r){
        this.p = p;
        this.r = r;
        this.baseR = r;
        spd = 0f;
        deviation = 40f;
        alpha = 20;
        strokeWeight = 1;
        resolution = 20;
        angleOffset = 0;
        center = new Point(p.width/2, p.height/2);
    }

    @Override
    public void draw(SoundAnalysis sa) {
        r+= spd;
        float avg = sa.getAvg(sa.spectrum, (int)PApplet.map(r,50,50+4*100, 0, 256));
        Point lastTarget = null;
        if(sa.peak){
            r+=5;
        }else {
            if(r>baseR){
                r--;
            }
        }
        float oneStep = 360f/resolution;
        for(int i = 0; i < resolution + 1; i++){
            float angle = i*oneStep + angleOffset;
            Point target;
            if(i%2==0){
                target = getPointAtAngle(center, r + avg*deviation, angle );
            }else{
                target = getPointAtAngle(center, r - avg*deviation, angle );
            }

            p.stroke(255,0,0, alpha);
            p.strokeWeight(strokeWeight);
            if(lastTarget != null){
                p.line(target.x, target.y, lastTarget.x, lastTarget.y);
            }
            lastTarget = target;
        }
    }



    /**
     * Finds a point in a given angle and distance from a center point.
     *
     * @param center center point * @param radius given distance
     * @param angle  given angle * @return
     */
    private Point getPointAtAngle(Point center, float radius, float angle) {
        return new Point(center.x + radius * p.cos(angle * p.PI / 180), center.y + radius * p.sin(angle * p.PI / 180));
    }

    /**
     * Returns angle of a line vs the horizon
     *
     * @param origin start of the line
     * @param end    end of the line 🚆
     * @return horizontal line returns 0, vertical line returns -90 or 90
     */
    private float getAngle(Point origin, Point end) {
        return p.degrees(p.atan2(end.y - origin.y, end.x - origin.x));
    }

    private class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}