import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import processing.sound.*;

import com.hamoid.VideoExport;

public class MainApp extends PApplet{

//    concentric circles growing outward made out of various icons / gifs even? one icon type per circle
//    background generated from icon for good contrast?

    boolean first = true;
    int spawnrate = 30;
    int bgColor = 00;
    int bgAlpha = 15;

    BandFactory bf;
    List<Band> bandsOnScreen;


    //////////////
    // ON AIR   //
    //////////////

    boolean rec = true;
    String ostSourcePath = "";//D:\Music\The Black Angels\Passover\05 Black Grease.mp3
    boolean playSong = false;
    SoundFile soundFile;

    boolean fadeout = false;
    VideoExport vid;
    float fadeMag = 0;
    float fadeSpd = 3f;
    float fadeMin = 0;

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
        fullScreen();
        //size(1200,800);
        smooth(8);
    }

    public void setup(){
        bandsOnScreen = new ArrayList<Band>();
        bf = new BandFactory(this);

        noStroke();
        fill(bgColor);
        rect(0,0,width,height);

        if(rec){
            vid = new VideoExport(this);
            if(ostSourcePath!=""){
                vid.setAudioFileName(ostSourcePath);
                if(playSong){

                }
            }
            vid.startMovie();
        }
    }

    int spawnedAlready = 0;
    boolean pause = false;
    public void draw() {
//        paintCrosshairs();
        if(!pause){
            //background
            noStroke();
            fill(bgColor, bgAlpha);
            rect(0, 0, width, height);
            //spawn new bands
            if (first || (frameCount % spawnrate == 0)) {
                bandsOnScreen.add(bf.getNewBand(spawnedAlready++));
                first = false;
            }
            //draw bands
            for (Band b : bandsOnScreen) {
                b.update();
                b.draw();
            }


            removeAllOffscreenBands();

            if (fadeout) {
                if (fadeMag < 255 - fadeMin) {
                    tint(255 - (fadeMag += fadeSpd));
                } else {
                    tint(fadeMin);
                }
            }
            if (rec) {
                vid.saveFrame();
                println("elapsed: " + vid.getCurrentTime());
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
    }

    void removeAllOffscreenBands(){
        List<Band> toRemove = new ArrayList<Band>();
        for(Band l : bandsOnScreen){
            if(l.loc > width + height){
                toRemove.add(l);
            }
        }
        bandsOnScreen.removeAll(toRemove);
    }










}