import Drawables.Drawable;
import Factories.DrawableFactory;
import Factories.ScatterplotWaveformFactory;
import beads.*;
import org.joda.time.DateTime;
import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import processing.core.PImage;


import com.hamoid.VideoExport;

public class MainApp extends PApplet{



    private int bgColor = 00;
    private int bgAlpha = 15;

    private List<DrawableFactory> drawableFactories;
    private List<Drawable> drawablesShown;
    private List<PImage> imageStore;
    private String imageDirectory = "C:\\Users\\Jakub\\Desktop\\196758-alerts\\png";

    //////////////////////////////
    private boolean rec = false;//
    private boolean playSong = true;
    private boolean visualiser = true;
    // ON AIR  (cpu expensive)  //
    //////////////////////////////

    private AudioContext ac;
    private PowerSpectrum ps;

    private VideoExport vid;
    private boolean fadeout = false;
    private float fadeMag = 0;
    private float fadeSpd = 3f;
    private float fadeMin = 0;

    private boolean flagA = true;
    private boolean flagB = true;
    private boolean flagC = true;

    private long animationStarted;

    private String ostFilepath = "D:\\Music\\The Black Angels\\Passover\\05 Black Grease.mp3";


    public static void main(String[] args)
    {
        PApplet.main("MainApp", args);
    }

    public void paintCrosshairs(){
        stroke(200,0,0);
        line(0, height/2, width, height/2);
        line(width/2, 0, width / 2, height);
    }

    public void settings()
    {
        //fullScreen();
        size(1200,800);
        smooth(8);
    }

    public void setup(){
        drawablesShown = new ArrayList<Drawable>();
        drawableFactories = new ArrayList<DrawableFactory>();

        imageStore = loadImagesFromDisk(imageDirectory);

        //draw background
        noStroke();
        fill(bgColor);
        rect(0,0,width,height);

        //record the show, start the music
        if(rec){
            vid = new VideoExport(this);
            vid.setAudioFileName(ostFilepath);
            vid.startMovie();
        }

        setupAudio(new File(ostFilepath));
        ac = new AudioContext();

        animationStarted = DateTime.now().getMillis();
    }

    private boolean pause = false;
    public void draw() {

//        paintCrosshairs();
        if(!pause){
            //background
            noStroke();
            fill(bgColor, bgAlpha);
            rect(0, 0, width, height);

            tryAddNewFactories();
            float[] spectrum = ps.getFeatures();
            //draw bands
            for (Drawable b : drawablesShown) {
                b.draw(spectrum);
            }

            //re-analyze bands in audio, proc the factories
            for (DrawableFactory f : drawableFactories){
                drawablesShown = f.update(drawablesShown,spectrum);
            }

            if (fadeout) {
                if (fadeMag < 255 - fadeMin) {
                    tint(255 - (fadeMag += fadeSpd));
                } else {
                    tint(fadeMin);
                }
            }
            if (rec) {
                vid.saveFrame();
               //println("video time: " + vid.getCurrentTime());
            }
        }

        if(keyPressed){
            if(key=='w' && rec) {
                vid.endMovie();
                rec = false;
            }
            if(key=='f'){
                fadeout = true;
            }
        }
        if(keyPressed && key=='p'){
            pause = true;
        }else{
            pause = false;
        }
        println("datetime ms: " + getSecondsElapsed());
    }



    private ArrayList<PImage> loadImagesFromDisk(String imgSourceDir){
        ArrayList<PImage> images = new ArrayList<PImage>();
        List<String> imgFilenames = getFilenamesInDirectory(imgSourceDir);
        for(String s : imgFilenames){
            images.add(loadImage(imgSourceDir + "\\" + s));
        }
        println("-----------------");
        println("imgs loaded:" + images.size());
        return images;
    }

    private ArrayList<String> getFilenamesInDirectory(String dir){
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


    private void tryAddNewFactories(){
        if(flagA){
            //drawableFactories.add(new BandFactory(this, 0, 50, imageStore));
            flagA = false;
        }
        if(flagB && getSecondsElapsed() > 1){
            //drawableFactories.add(new BandFactory(this, 1, 50, imageStore));
            flagB = false;
        }
        if(flagC && getSecondsElapsed() > -1){
            drawableFactories.add(new ScatterplotWaveformFactory(this));
            flagC = false;
        }
    }

    private long getSecondsElapsed(){
        return DateTime.now().minus(animationStarted).getMillis()/1000;
    }

/*
 * This code is used by the selectInput() method to get the filepath.
 */
    private void setupAudio(File selection) {
        //play song
        if(selection != null){
            String audioFileName = selection.getAbsolutePath();
            SamplePlayer player = new SamplePlayer(ac, SampleManager.sample(audioFileName));
            Gain g = new Gain(ac, 2, 0.2f);
            g.addInput(player);
            ac.out.addInput(g);
        }
        //setup fft
        ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
        sfs.addInput(ac.out);
        FFT fft = new FFT();
        ps = new PowerSpectrum();
        sfs.addListener(fft);
        fft.addListener(ps);
        ac.out.addDependent(sfs);
        ac.start();
    }

}