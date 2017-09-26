import processing.core.PApplet;

public class SuicideBand extends Band {

    public SuicideBand(PApplet p) {
        super(p);

    }

    int timesDirectionChanged = 0;
    int maxCenterPasses = 3;
    boolean oldHeading = true;

    @Override
    public void draw() {


        super.draw();

    }

    @Override
    protected float genSize() {
        return super.genSize();
    }

    @Override
    protected float genForce() {
        float newForce = p.cos(loc/200)/100;

        if(timesDirectionChanged > maxCenterPasses){
            if(newForce<0){
                newForce*=-1;
            }
        }

        boolean newHeading = false;
        if(newForce > 0){
            newHeading = true;
        }

        if(newHeading != oldHeading)
        {
            oldHeading = newHeading;
            timesDirectionChanged++;
            p.println(timesDirectionChanged);
        }

        return newForce;
    }



}
