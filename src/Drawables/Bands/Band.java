package Drawables.Bands;

import Drawables.Abstract.Drawable;
import Sound.SoundAnalysis;
import processing.core.PApplet;
import processing.core.PImage;

import Math.*;

import static Math.Angle.getAngle;

public class Band extends Drawable {
    PApplet p;

    public Band(PApplet p) {
        this.p = p;
    }

    public PImage img;
    public float loc, spd, acc;
    public float rot, rSpd, rAcc;
    public float size, sizeMod;
    public int imgCount;
    public Point coords;

    public void draw(SoundAnalysis sa) {
        update(sa.getSpectrum());
        float angleStep = 360 / imgCount;
        for (int i = 0; i < imgCount; i++) {
            coords = Angle.getPointAtAngle(
                    new Point(p.width / 2, p.height / 2),
                    loc,
                    rot + angleStep * i
            );
            size = genSize();
            p.pushMatrix();
                p.translate(coords.x, coords.y);
                p.rotate(Angle.toRad(
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
}

