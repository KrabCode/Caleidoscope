package Sound;

public class SoundAnalysis {
    float[] spectrum;
    boolean peak;

    public float getAvg(float[] src){
        float total = 0;
        for(float f : src){
            total += f;
        }
        return total / src.length;
    }

    public float getAvg(float[] src, Range range){
        if(src != null){
            float total = 0;
            for(int i = range.getFrom(); i < range.getTo(); i++){
                total += src[i];
            }
            return total / (range.getTo() - range.getFrom());
        }
        return 0;

    }

    public float[] getSpectrum() {
        return spectrum;
    }

    public boolean getPeak() {
        return peak;
    }
}
