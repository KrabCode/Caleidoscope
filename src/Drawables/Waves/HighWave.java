package Drawables.Waves;

import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class HighWave extends Wave{


    public HighWave(PApplet p, float r, float deviation) {
        super(p, r, deviation);
    }

    @Override
    public Range getRange(SoundAnalysis sa) {
        return new Range(sa.getSpectrum().length/8, sa.getSpectrum().length/4);
    }
}
