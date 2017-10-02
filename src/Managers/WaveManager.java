package Managers;

import Drawables.Abstract.Drawable;
import Drawables.Waves.MidWave;
import Drawables.Waves.HighWave;
import Drawables.Waves.LowWave;
import Drawables.Waves.Wave;
import Managers.Abstract.Manager;
import Sound.Range;
import Sound.SoundAnalysis;
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

        Wave low = new LowWave(p, 50);
        Wave all = new MidWave(p, 150);
        Wave high = new HighWave(p, 250);

        wavesManaged.add(low);
        wavesManaged.add(all);
        wavesManaged.add(high);
    }
    int i = 0;
    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {

        //ON START
        if(initFlag)
        {
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


    public int[] getPointsOfInterest(int spectrumLength){
        if(wavesManaged !=null && wavesManaged.size() > 0){
            int[] results = new int[wavesManaged.size()-1];
            int resultIndex = 0;
            int loopIndex = 0;
            for(Wave w : wavesManaged){
                Range r = w.getRange(spectrumLength);
                if(loopIndex != 0 && loopIndex < wavesManaged.size()) {
                    results[resultIndex++] = r.getFrom(); //if not first, add start
                }
                loopIndex++;
            }
            return results;
        }
        return new int[]{};
    }


}