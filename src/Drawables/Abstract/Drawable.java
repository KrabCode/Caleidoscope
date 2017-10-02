package Drawables.Abstract;

import Sound.SoundAnalysis;

public abstract class Drawable {
    /**
     * MainApp calls this method every frame
     *
     * @param sa Sound Analysis including but not limited to the FFT of the current sound wave and peak detection
     */
    abstract public void draw(final SoundAnalysis sa);
}
