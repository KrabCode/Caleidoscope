import processing.core.PApplet;

public class CircularEnvelopBand extends Band {
    public CircularEnvelopBand(PApplet p) {
        super(p);
    }

    @Override
    protected float genRforce() {

        float f = p.cos(loc)/500;
        if(rSpd < 0){
            if(f > 0){
                f*=-1;
            }
        }else{
            if(f < 0){
                f*=-1;
            }
        }
        return f;
    }
}
