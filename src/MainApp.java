import drawable.Drawable;
import drawable.Manager;
import drawable.bands.BandManager;
import drawable.diag.SoundSpectrumViewerManager;
import drawable.waves.WaveManager;
import processing.core.*;
import sound.Range;
import sound.SoundAnalysis;
import sound.SoundManager;
import utils.IO;
import utils.Recorder;
import utils.Timekeeper;
import utils.geometry.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * The main class.
 *
 * extending the PApplet is required by the IDE
 *
 * See https://processing.org/tutorials/eclipse/ for why
 * and what the flip is going on with passing
 * PApplet references to every constructor
 */
public class MainApp extends PApplet{

    private int bgColor = 00; //main canvas background color
    private int bgAlpha = 15; //main canvas background alpha

    private List<Manager> activeManagers;       //managing lifecycles of drawable
    private List<Drawable> activeDrawables;     //drawable being displayed

    //  ON AIR
    //////////////////////////////
    private boolean rec   = false;
    //////////////////////////////
    //

    //canvas dimensions
    private boolean fullScreen = true;
    private int windowWidth = 800;
    private int windowHeight = 800;
    //

    //simple canvaswide effects
    private boolean pause = false;

    private boolean fadeout = false;
    private float fadeMag = 0;
    private float fadeSpd = 3f;
    private float fadeMin = 0;
    //

    private Recorder recorder;  //wrapper for the org.hamoid.VideoExport library
    private SoundManager sm;    //wrapper for the Beads library



    private boolean promptUserForSoundtrack = false;
    private String defaultSoundtrackDirectory = "C:\\Users\\Jakub\\Desktop\\Clutter\\Roadtrip\\Psychedelic\\";
    public static List<File> userSelectedSoundtrack;
    private float peakThreshold = .2f;  //peak detection threshold, lower means peaks happen less often
    //TODO automate setting peak threshold to approximate a given number of peaks / minute

    private List<PImage> imageStore;
    private String imageDirectory = null; //"C:\\Users\\Jakub\\Desktop\\196758-alerts\\png";

    private boolean flagA = true;  //concentric circles
    private boolean flagB = false;  //frequency cutting viewer
    private boolean flagC = false; //bands (requires non null imageDirectory)

    public static void main(String[] args)
    {
        PApplet.main("MainApp", args);
    }

    /**
     * Happens once at the start. Only useful for setting window size / fullscreen and smooth.
     * Required because IDE
     * https://processing.org/tutorials/eclipse/
     */
    public void settings()
    {
        if(fullScreen){
            fullScreen();
        }else{
            size(windowWidth,windowHeight);
        }
        //smooth(8);
    }

    /**
     * Happens once at the start
     */
    public void setup(){
        //instantiate stuff
        activeDrawables = new ArrayList<Drawable>();
        activeManagers = new ArrayList<Manager>();
        sm = new SoundManager(this, userSelectedSoundtrack, peakThreshold, defaultSoundtrackDirectory);

        if(imageDirectory!=null && !imageDirectory.equals("")){
            imageStore = IO.loadImagesFromDisk(this, imageDirectory);
        }

        //draw background
        noStroke();
        fill(bgColor);
        rect(0,0,width,height);

        //record the show
        if(rec){
            recorder = new Recorder(this);
        }

        //the show has started, remember the time
        Timekeeper.getTimer().setStart();
    }

    /**
     * Happens every frame
     */
    public void draw() {
//        paintCrosshairs(); // symmetry check
        checkInput();
        if(!pause){
            SoundAnalysis sa = sm.getFreshAnalysis();

            //draw background
            fill(bgColor, bgAlpha);
            noStroke();
            rect(0, 0, width, height);

            //draw all drawable
            for (Drawable b : activeDrawables) {
                b.draw(sa);
            }

            //instantiate new managers if needed
            tryInstantiateManagers();
            for (Manager f : activeManagers){
                //update every manager object
                activeDrawables = f.update(activeDrawables,sa);
            }
            if (fadeout) {
                if (fadeMag < 255 - fadeMin) {
                    tint(255 - (fadeMag += fadeSpd));
                } else {
                    tint(fadeMin);
                }
            }
            if (rec) {
                recorder.saveFrame();
            }
        }
        bgColor = 0;
    }

    WaveManager wm; //only used in tryInstantiateManagers
    /**
     * Attempts to construct activeManagers
     * (churning out parts of the show)
     * in effect deploying them at set moments.
     */
    private void tryInstantiateManagers(){
        if(flagA){
            wm = new WaveManager(this);
            activeManagers.add(wm);
            flagA = false;
        }
        if(flagB && Timekeeper.getTimer().getMsElapsed()>1000){
            //need to wait a tiny little bit for the sound analysis to kick in
            // so we can ask how many spectrum parts there are
            List<Range> markedFrequencies = wm.getRangesBeingVisualised();
            activeManagers.add(new SoundSpectrumViewerManager(
                    this,
                    //put the viewport into the lower right corner
                    new Point(0, height-100),
                    new Point(width, height),
                    markedFrequencies
            ));
            flagB = false;
        }
        if(flagC && Timekeeper.getTimer().getMsElapsed() > -1){
            activeManagers.add(new BandManager(this, 0, 50, imageStore));
            flagC = false;
        }
    }

    public void paintCrosshairs(){
        stroke(200,0,0);
        line(0, height/2, width, height/2);
        line(width/2, 0, width / 2, height);
    }

    public void printToScreen(String s){
        //TODO print information, such as the current song or error to screen
    }

    private void checkInput(){
        if(keyPressed){
            if(key=='w' && rec) {
                recorder.endRecording();
                rec = false;
            }
            if(key=='f'){
                fadeout = true;
            }
            if(key=='.'){
                sm.tryPlayNext();
            }
            if(key==','){
                //sm.tryPlayNext();
            }
        }

        if(keyPressed && key=='p'){
            pause = true;
        }else{
            pause = false;
        }
    }
}