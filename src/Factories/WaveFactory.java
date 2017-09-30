package Factories;

import Drawables.Drawable;
import Drawables.Waves.Wave;
import Sound.SoundAnalysis;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class WaveFactory extends DrawableFactory {
    int spawnrate = 80;
    List<Wave> wavesFromThisFactory;
    PApplet p;

    public WaveFactory(PApplet p){
        this.p = p;
        wavesFromThisFactory = new ArrayList<Wave>();
    }
    int i = 0;
    @Override
    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa) {

        if(/*p.frameCount % spawnrate == 0 ||*/ p.frameCount == 1)
        {
            for(int j = 0; j < 4; j++){
                Wave w = new Wave(p, 50+(i++)*100);
                wavesFromThisFactory.add(w);
                drawables.add(w);
            }
        }
        return drawables;
    }
}