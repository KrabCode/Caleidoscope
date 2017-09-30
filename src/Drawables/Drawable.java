package Drawables;

import Sound.SoundAnalysis;

public abstract class Drawable {
    //MainApp calls this every frame
    abstract public void draw(SoundAnalysis sa);
}
