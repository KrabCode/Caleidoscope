package Managers;

import Drawables.Abstract.Drawable;
import Drawables.Waves.MidWave;
import Drawables.Waves.HighWave;
import Drawables.Waves.LowWave;
import Drawables.Waves.Wave;
import Managers.Abstract.Manager;
import Sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class WaveManager extends Manager {
    int spawnrate = 80;
    List<Wave> wavesFromThisFactory;
    PApplet p;

    public WaveManager(PApplet p){
        this.p = p;
        wavesFromThisFactory = new ArrayList<Wave>();
    }
    int i = 0;
    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {

        if(/*p.frameCount % spawnrate == 0 ||*/ p.frameCount == 1)
        {

            Wave low = new LowWave(p, 50, 5);
            Wave all = new MidWave(p, 200, 40);
            Wave high = new HighWave(p, 400, 160);

            wavesFromThisFactory.add(low);
            wavesFromThisFactory.add(all);
            wavesFromThisFactory.add(high);

            drawables.add(low);
            drawables.add(all);
            drawables.add(high);

//             Samey concentric waves:

//            for(int j = 0; j < 4; j++){
//                Wave w = new Wave(p, 50+(i++)*100);
//                wavesFromThisFactory.add(w);
//                drawables.add(w);
//            }

        }
        return drawables;
    }
}