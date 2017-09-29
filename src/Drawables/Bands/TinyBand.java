package Drawables.Bands;

import processing.core.PApplet;

public class TinyBand extends Band {
    public TinyBand(PApplet p) {
        super(p);
    }

    @Override
    protected float genRforce() {
        if(rSpd > 0){
            return 0.0001f;
        }else{
            return-0.0001f;
        }
    }
}
