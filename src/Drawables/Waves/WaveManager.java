package Drawables.Waves;

import Drawables.Abstract.Drawable;
import Drawables.Abstract.Manager;
import Sound.Range;
import Sound.SoundAnalysis;
import Sound.SoundManager;
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
            Wave low = new Wave(p, 200, new Range(0, 10), 4f);
            Wave mid = new Wave(p, 350, new Range(10,40), 8);
            Wave high = new Wave(p, 500, new Range(40,80), 16);

            wavesManaged.add(low);
            wavesManaged.add(mid);
            wavesManaged.add(high);
            drawables.addAll(wavesManaged);
            initFlag = false;
        }

//          Samey concentric waves:

//            for(int j = 0; j < 4; j++){
//                Wave w = new Wave(p, 50+(i++)*100);
//                wavesManaged.add(w);
//                drawables.add(w);
//            }

        return drawables;
    }


    public List<Range> getRangesBeingVisualised(){
        List<Range> results = new ArrayList<>();
        for(Wave w : wavesManaged){
            results.add(w.range);
        }
        return results;
    }
}