import Drawables.Drawable;
import Factories.DrawableFactory;
import Factories.WaveFactory;
import Sound.SoundAnalysis;
import Sound.SoundManager;
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
    private String imageDirectory = "";//"C:\\Users\\Jakub\\Desktop\\196758-alerts\\png";

    //////////////////////////////
       boolean rec = false ;     //
//         ON AIR                   //
    //////////////////////////////

    private boolean pause = false;

    private VideoExport vid;
    private boolean fadeout = false;
    private float fadeMag = 0;
    private float fadeSpd = 3f;
    private float fadeMin = 0;

    private SoundManager sm;
    //private String ostFilepath = "C:\\Users\\Jakub\\Downloads\\True Detective - Intro  Opening Song - Theme (The Handsome Family - Far From Any Road) + LYRICS.mp3";
    private String ostFilepath = "D:\\Music\\The Black Angels\\Passover\\05 Black Grease.mp3";

    private boolean flagA = true;
    private boolean flagB = true;
    private boolean flagC = true;

    private long animationStartedEpochMs;

    private boolean playSong = true;



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
//        fullScreen();
        size(600,600);
        smooth(8);
    }

    public void setup(){
        drawablesShown = new ArrayList<Drawable>();
        drawableFactories = new ArrayList<DrawableFactory>();
        sm = new SoundManager(ostFilepath, playSong, 0.2f);

        if(imageDirectory!=null&&!imageDirectory.equals("")){
            imageStore = loadImagesFromDisk(imageDirectory);
        }

        //draw background
        noStroke();
        fill(bgColor);
        rect(0,0,width,height);

        //record the show, start the music
        if(rec){
            vid = new VideoExport(this);
            if(playSong){
                vid.setAudioFileName(ostFilepath);
            }
            vid.startMovie();
        }

        //the show has started, remember the time
        animationStartedEpochMs = DateTime.now().getMillis();
    }

    private void tryAddNewFactories(){
        if(flagA){
            drawableFactories.add(new WaveFactory(this));
            flagA = false;
        }
        if(flagB && getSecondsElapsed() > 1){
            //drawableFactories.add(new BandFactory(this, 1, 50, imageStore));
            flagB = false;
        }
        if(flagC && getSecondsElapsed() > -1){
            //drawableFactories.add(new BandFactory(this, 0, 50, imageStore));
            flagC = false;
        }
    }


    public void draw() {
//        paintCrosshairs();
        checkInput();
        if(!pause){
            SoundAnalysis sa = sm.getFreshAnalysis();

            if(sa.peak){
                fill(3,0,0, bgAlpha);
            }else{
                fill(bgColor, bgAlpha);
            }
            //draw background
            noStroke();
            rect(0, 0, width, height);

            //draw bands
            for (Drawable b : drawablesShown) {
                b.draw(sa);
            }

            //proc the factories
            tryAddNewFactories();
            for (DrawableFactory f : drawableFactories){
                drawablesShown = f.update(drawablesShown,sa);
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

        bgColor = 0;
    }

    private void checkInput(){
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

    private int getSecondsElapsed(){
        return  (int) (DateTime.now().minus(animationStartedEpochMs).getMillis()/1000);
    }
}