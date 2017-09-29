package Factories;

import Drawables.Drawable;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class DrawableFactory {
    //MainApp calls this every frame
    public abstract List<Drawable> update(List<Drawable> drawables, float[] spectrum);

}
