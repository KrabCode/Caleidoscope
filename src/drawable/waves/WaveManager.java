package drawable.waves;

import drawable.Drawable;
import drawable.Manager;
import sound.Range;
import sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class WaveManager extends Manager {
    boolean initFlag = true;
    int spawnrate = 80;
    List<Wave> wavesManaged;
    PApplet p;

    public WaveManager(PApplet p){
        this.p = p;
        wavesManaged = new ArrayList<Wave>();


    }
    int i = 0;
    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {

        //ON START
        if(initFlag)
        {
            float spectrumSize = sa.getSpectrum().length;
            Wave low = new Wave(p, 200, new Range(0, 10), 4);
            Wave mid = new Wave(p, 350, new Range(11,40), 16);
            Wave high = new Wave(p, 500, new Range(41,80), 80);
            wavesManaged.add(low);
            wavesManaged.add(mid);
            wavesManaged.add(high);
            initFlag = false;
            drawables.addAll(wavesManaged);
        }

//          Samey concentric waves:

//            for(int j = 0; j < 4; j++){
//                Wave w = new Wave(p, 50+(i++)*100);
//                wavesManaged.add(w);
//                drawable.add(w);
//            }

        return drawables;
    }

    public void setWaveToWatchRange(int wave, Range range){
        wavesManaged.get(wave).range = range;
    }

    public List<Range> getRangesBeingVisualised(){
        List<Range> results = new ArrayList<>();
        for(Wave w : wavesManaged){
            results.add(w.range);
        }
        return results;
    }
}