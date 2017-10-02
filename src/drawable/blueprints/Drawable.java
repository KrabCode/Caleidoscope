package drawable.blueprints;

import sound.SoundAnalysis;

public abstract class Drawable {
    /**
     * MainApp calls this method every frame
     *
     * @param sa sound Analysis including but not limited to the FFT of the current sound wave and peak detection
     */
    abstract public void draw(final SoundAnalysis sa);
}
