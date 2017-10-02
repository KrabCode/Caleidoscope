package Drawables.Waves;

import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class HighWave extends Wave{


    public HighWave(PApplet p, float r) {
        super(p, r);
    }

    @Override
    public Range getRange(int spectrumLength) {
        return new Range(spectrumLength/8, spectrumLength/4);
    }

    @Override
    public float getDeviation() {
        return 80;
    }
}
