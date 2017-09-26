import processing.core.PApplet;

public class SlitheringRaycasterBand extends Band{

    public SlitheringRaycasterBand(PApplet window) {
        super(window);
    }



    @Override
    protected float genRforce() {
        return p.cos(rot/20)/500;
    }

}


