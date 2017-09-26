import processing.core.PApplet;

public class TinyBand extends Band {
    public TinyBand(PApplet p) {
        super(p);
    }

    @Override
    protected float genRforce() {
        return 0.000001f;
    }

    @Override
    protected float genSize() {
        return spd;
    }
}
