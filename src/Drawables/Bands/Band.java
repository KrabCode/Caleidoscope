package Drawables.Bands;

import Drawables.Drawable;
import Sound.SoundAnalysis;
import processing.core.PApplet;
import processing.core.PImage;

public class Band extends Drawable {
    PApplet p;

    public Band(PApplet window) {
        this.p = window;
    }

    public PImage img;
    public float loc, spd, acc;
    public float rot, rSpd, rAcc;
    public float size, sizeMod;
    public int imgCount;
    public Point coords;

    public void draw(SoundAnalysis sa) {
        update(sa.spectrum);
        float angleStep = 360 / imgCount;
        for (int i = 0; i < imgCount; i++) {
            coords = getPointAtAngle(
                    new Point(p.width / 2, p.height / 2),
                    loc,
                    rot + angleStep * i
            );
            size = genSize();
            p.pushMatrix();
            p.translate(coords.x, coords.y);
            p.rotate(toRad(
                    -90 + getAngle(
                            new Point(coords.x, coords.y),
                            new Point(p.width / 2, p.height / 2)
                    )));
            p.image(img, -size / 2, -size / 2, size, size);
            p.popMatrix();
        }
    }

    public void applyForce(float acc, float rAcc) {
        this.acc += acc;
        this.rAcc += rAcc;
    }

    protected void genAudioReaction(float mag){
        //applyForce(mag/500, mag/1000);
    }

    protected float genSize() {
        return sizeMod * spd;
    }

    protected float genForce() {
        return 0;
    }

    protected float genRforce() {
        return 0;
    }

    private void update(float[] spectrum) {

        applyForce(genForce(), genRforce());

        //get lower third avg
        float total = 0;
        for(int i = 0; i < spectrum.length/3; i++){
            total+= spectrum[i];
        }
        total /= spectrum.length/3;

        genAudioReaction(total);
        spd += acc;
        rSpd += rAcc;
        loc += spd;
        rot += rSpd;

        acc = 0;
        rAcc = 0;
    }
    /**
     * Convert me like one of your french girls.
     *
     * @param degrees
     * @return
     */
    private float toRad(float degrees){
        return (float) java.lang.Math.toRadians(degrees);
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
     * Returns angle of a line vs the horizont *
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

