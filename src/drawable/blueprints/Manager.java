package drawable.blueprints;

import sound.SoundAnalysis;

import java.util.List;

public abstract class Manager {
    /**
     * MainApp calls this every frame
     */
    public abstract List<Drawable> update(List<Drawable> drawables, final SoundAnalysis sa);

}
