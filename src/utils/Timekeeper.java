package utils;

import org.joda.time.DateTime;

public class Timekeeper {

    private static long animationStartedEpochMs = 0;
    private static Timekeeper t;

    private Timekeeper(){}

    public static Timekeeper getTimer(){
        if(t == null){
            return new Timekeeper();
        }else{
            return t;
        }
    }

    public void setStart(){
        animationStartedEpochMs = DateTime.now().getMillis();
    }

    public float getMsElapsed(){
        return (DateTime.now().minus(animationStartedEpochMs).getMillis());
    }
}
