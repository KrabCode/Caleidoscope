import processing.core.PApplet;
import processing.core.PImage;

import java.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.math.*;
import java.util.Random;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

public class BandFactory {
    protected PApplet p ;

    List<PImage> imageStore ;



    private BandFactory(){}

    public BandFactory(PApplet p){
        this.p = p;
        loadImagesFromDisk();
    }

    boolean odd = true;

    public Band getNewBand(int i) {

        int type = i % 4;
       // int type = 0;
        Band b = null;
        switch(type){
            /**
             *SLITHER OUT
             */
            case 0:{
                b  = new SlitheringRaycasterBand(p);
                float startAcc = 0.8f;
                float startRacc = 0f;
                float startRspd = 0f;
                float sizeModifier = 30;
                int imgCount = 8;

                b.imgCount = imgCount;
                b.acc = startAcc;
                b.img = getRandomUnusedImage();
                b.rot = i *20;
                b.rAcc = startRacc;
                b.rSpd = startRspd;

                b.sizeMod = sizeModifier;
                break;
            }

            /**
             TINY BASTARDS SPINNING OUT OF CONTROL
             */
            case 1: {
                {
                    b = new TinyBand(p);
                    float startAcc = 4f;
                    float startRacc = 0f;
                    float startRspd = 1.8f;
                    float sizeModifier = 30;
                    int imgCount = 12;

                    b.imgCount = imgCount;
                    b.acc = startAcc;
                    b.img = getRandomUnusedImage();
                    b.rAcc = startRacc;
                    b.rSpd = startRspd;
                    b.sizeMod = sizeModifier;
                }
                break;
            }

            /**
             * STATIONARY SATELLITE
             */
            case 2: {
                {
                    b = new CircularEnvelopBand(p);
                    float startAcc = 0.8f;
                    float startRacc = 0f;
                    float startRspd = 0.01f;
                    float sizeModifier = 20;
                    int imgCount = 20;

                    b.imgCount = imgCount;
                    b.acc = startAcc;
                    b.img = getRandomUnusedImage();
                    odd = !odd;
                    if(odd){
                        b.rAcc = startRacc;
                        b.rSpd = startRspd;
                    }
                    else{
                        b.rAcc = -startRacc;
                        b.rSpd = -startRspd;
                    }
                    b.sizeMod = sizeModifier;
                }
                break;
            }

            /**
             * CRAZY SWAYZE
             */
            case 3: {
                {
                    b = new SuicideBand(p);
                    float startAcc = 0.8f;
                    float startRacc = 0f;
                    float startRspd = 0f;
                    float sizeModifier = 10;
                    int imgCount = 10;

                    b.imgCount = imgCount;
                    b.acc = startAcc;
                    b.img = getRandomUnusedImage();

                    b.rAcc = startRacc;
                    b.rSpd = startRspd;

                    b.sizeMod = sizeModifier;
                }
                break;
            }
        }
        return b;
    }


    private PImage getRandomUnusedImage() {
        Random generator = new Random();
        int i = generator.nextInt(imageStore.size()) + 1;
        return imageStore.get(i);
    }

    protected String imgSourceDir = "C:\\Users\\Jakub\\Desktop\\148705-essential-collection\\png";

    public void loadImagesFromDisk(){
        imageStore = new ArrayList<PImage>();
        List<String> imgFilenames = getFilenamesInDirectory(imgSourceDir);
        for(String s : imgFilenames){
            imageStore.add(p.loadImage(imgSourceDir + "\\" + s));
        }
        p.println("-----------------");
        p.println("imgs loaded:" + imageStore.size());
    }
    ArrayList<String> getFilenamesInDirectory(String dir){
        ArrayList<String> filenames = new ArrayList<String>();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                filenames.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return filenames;
    }
}


