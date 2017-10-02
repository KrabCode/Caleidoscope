package Drawables.Waves;

import Drawables.Waves.Wave;
import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class MidWave extends Wave {

    public MidWave(PApplet p, float r) {
        super(p, r);
    }

    @Override
    public Range getRange(int spectrumLength) {
        return new Range(spectrumLength / 16, spectrumLength / 8);
    }

    @Override
    public float getDeviation() {
        return 20;
    }
}
