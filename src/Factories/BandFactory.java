package Factories;

import Drawables.*;
import Drawables.Bands.*;
import Sound.SoundAnalysis;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BandFactory extends DrawableFactory {

    int mold = 0;
    boolean odd = true;
    int spawnrate = 30;
    List<Band> bandsFromThisFactory;
    List<PImage> imageStore;


    private PApplet p ;

    private BandFactory(){}
    public BandFactory(PApplet p, int mold, int spawnrate, List<PImage> imageStore){
        this.p = p;
        this.mold = mold;
        this.spawnrate = spawnrate;
        this.imageStore = imageStore;
        bandsFromThisFactory = new ArrayList<Band>();
        bandsFromThisFactory.add(getNewBand());
    }


    public List<Drawable> update(List<Drawable> drawables, SoundAnalysis sa){
        if(p.frameCount % spawnrate == 0)
        {
            Band newBand = getNewBand();
            bandsFromThisFactory.add(newBand);
            drawables.add(newBand);
            drawables = removeAllOffscreenBands(drawables);
        }
        return drawables;
    }

    private List<Drawable> removeAllOffscreenBands(List<Drawable> drawablesShown){
        List<Band> toRemove = new ArrayList<Band>();
        for(Band l : bandsFromThisFactory){
            if(l.loc > p.width + p.height){
                toRemove.add(l);
            }
        }
        bandsFromThisFactory.removeAll(toRemove);
        drawablesShown.removeAll(toRemove);
        return drawablesShown;
    }

    private Band getNewBand() {
        // int type = 0;
        Band b = null;
        switch(mold){
            /**
             *SLITHER OUT
             */
            case 0:{
                b  = new SlitheringRaycasterBand(p);
                b.img = getRandomImage();
                b.imgCount = 40;
                b.acc = 0.8f;
                b.rot = 0;
                b.rAcc = 0f;
                b.rSpd = 0f;
                b.sizeMod = 10;
                break;
            }

            /**
             SPINNING OUT OF CONTROL
             */
            case 1: {
                b = new TinyBand(p);
                b.img = getRandomImage();
                b.imgCount = 12;
                b.acc = 0.3f;
                b.rAcc = 0f;
                if(odd){
                    b.rSpd = 1.8f;
                }else{
                    b.rSpd = -1.8f;
                }
                b.sizeMod = 10;
                break;
            }
        }
        return b;
    }

    private PImage getRandomImage() {
        Random generator = new Random();
        int i = generator.nextInt(imageStore.size());
        return imageStore.get(i);
    }



}


