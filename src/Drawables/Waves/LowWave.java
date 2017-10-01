package Drawables.Waves;

import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class LowWave extends Wave{


    public LowWave(PApplet p, float r, float deviation) {
        super(p, r, deviation);
    }

    @Override
    public Range getRange(SoundAnalysis sa) {
        return new Range(0,sa.getSpectrum().length / 16);
    }
}
