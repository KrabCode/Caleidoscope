package Factories;

import Drawables.Drawable;
import Sound.SoundAnalysis;

import java.util.List;

public abstract class DrawableFactory {
    //MainApp calls this every frame
    public abstract List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa);

}
