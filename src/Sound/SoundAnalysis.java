package Sound;

public class SoundAnalysis {
    public float[] spectrum;
    public boolean peak;

    public float getAvg(float[] src){
        float total = 0;
        for(float f : src){
            total += f;
        }
        return total / src.length;
    }

    public float getAvg(float[] src, int trim){
        float total = 0;
        int i = 0;
        for(float f : src){
            total += f;
            if(i++ > trim){
                break;
            }
        }
        return total / src.length;
    }
}
