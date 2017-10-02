package Drawables.Waves;

import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class LowWave extends Wave{


    public LowWave(PApplet p, float r) {
        super(p, r);
    }

    @Override
    public Range getRange(int spectrumLength) {
        return new Range(0, spectrumLength / 16);
    }

    @Override
    public float getDeviation() {
        return 1f;
    }
}
