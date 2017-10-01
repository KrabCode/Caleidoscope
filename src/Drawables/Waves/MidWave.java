package Drawables.Waves;

import Drawables.Waves.Wave;
import Sound.Range;
import Sound.SoundAnalysis;
import processing.core.PApplet;

public class MidWave extends Wave {

    public MidWave(PApplet p, float r, float deviation) {
        super(p, r, deviation);
    }

    @Override
    public Range getRange(SoundAnalysis sa) {
        return new Range(sa.getSpectrum().length / 16, sa.getSpectrum().length / 8);
    }
}
