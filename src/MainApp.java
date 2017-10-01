import Drawables.Abstract.Drawable;
import Managers.Abstract.Manager;
import Managers.SoundSpectrumViewerManager;
import Managers.WaveManager;
import Sound.SoundAnalysis;
import Sound.SoundManager;
import org.joda.time.DateTime;
import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import processing.core.PImage;
import com.hamoid.VideoExport;
import Math.*;

import javax.swing.*;

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



    private int bgColor = 00;
    private int bgAlpha = 15;

    private List<Manager> drawableFactories;
    private List<Drawable> drawablesShown;
    private List<PImage> imageStore;
    private String imageDirectory = null; //"C:\\Users\\Jakub\\Desktop\\196758-alerts\\png";


    //////////////////////////////   ON AIR
    private boolean rec             = false;
    private boolean playSong        = true;
    //////////////////////////////

    private boolean fullScreen = false;
    private int windowWidth = 800;
    private int windowHeight = 800;

    private boolean pause = false;

    private VideoExport vid;
    private int videoQualityPercent = 100;
    private int audioQualityKbps = 192;

    private boolean fadeout = false;
    private float fadeMag = 0;
    private float fadeSpd = 3f;
    private float fadeMin = 0;

    private SoundManager sm;
    //TODO private String ostFolderpath = "";
    //  private String ostFilepath = "D:\\Torrents\\Exuma\\Reincarnation\\06 Exuma - Pay Me What You Owe Me.mp3";
    // private String ostFilepath = "C:\\Users\\Jakub\\Downloads\\True Detective - Intro  Opening Song - Theme (The Handsome Family - Far From Any Road) + LYRICS.mp3";
    private float peakThreshold = .2f;
    private boolean promptUserForSoundtrack = false;
    //gets overwritten if
    private String ostFilepath = "C:\\Users\\Jakub\\Desktop\\Clutter\\Roadtrip\\Psychedelic\\07 · kyuss · space cadet.mp3";

    private boolean flagA = true;
    private boolean flagB = true;
    private boolean flagC = true;


    private static long animationStartedEpochMs = 0;

    //Create a file chooser
    private final JFileChooser fc = new JFileChooser();

    public static void main(String[] args)
    {
        PApplet.main("MainApp", args);
    }

    public void paintCrosshairs(){
        stroke(200,0,0);
        line(0, height/2, width, height/2);
        line(width/2, 0, width / 2, height);
    }

    /**
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


    public void printToScreen(String s){
        //TODO
    }

    /**
     * Happens once at the start
     */
    public void setup(){
        if(promptUserForSoundtrack){
            promptUserForSoundtrack();
        }

        //instantiate stuff
        drawablesShown = new ArrayList<Drawable>();
        drawableFactories = new ArrayList<Manager>();
        sm = new SoundManager(ostFilepath, playSong, peakThreshold);

        if(imageDirectory!=null && !imageDirectory.equals("")){
            imageStore = loadImagesFromDisk(imageDirectory);
        }

        //draw background
        noStroke();
        fill(bgColor);
        rect(0,0,width,height);

        //record the show
        if(rec){
            vid = new VideoExport(this);
            vid.setQuality(videoQualityPercent, audioQualityKbps);
            if(playSong){
                vid.setAudioFileName(ostFilepath);
            }
            vid.startMovie();
        }

        //the show has started, remember the time
        animationStartedEpochMs = DateTime.now().getMillis();
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

            //draw bands
            for (Drawable b : drawablesShown) {
                b.draw(sa);
            }

            //proc the factories
            tryAddNewFactories();
            for (Manager f : drawableFactories){
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
            }
        }
        bgColor = 0;
    }

    /**
     * The scripts
     */
    private void tryAddNewFactories(){
        if(flagA){
            drawableFactories.add(new WaveManager(this));
            flagA = false;
        }
        if(flagB){
            drawableFactories.add(new SoundSpectrumViewerManager(
                    this,
                    //put the viewport into the lower right corner
                    new Point(0, height-100),
                    new Point(256, height),
                    //sets the points of interest
                    new int[]{64,32,16}
                    ));
            flagB = false;
        }
        if(flagC && getSecondsElapsed() > -1){
            //drawableFactories.add(new BandManager(this, 0, 50, imageStore));
            flagC = false;
        }
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
            if(key=='o'){

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
        List<String> imgFilenames = IO.getFilenamesInDirectory(imgSourceDir);
        for(String s : imgFilenames){
            images.add(loadImage(imgSourceDir + "\\" + s));
        }
        return images;
    }

    private static float getSecondsElapsed() {
        return (DateTime.now().minus(animationStartedEpochMs).getMillis() / 1000);
    }

    File lastDir;
    public void promptUserForSoundtrack() {
        //Handle open button action.
        fc.setDialogTitle("Please select an .mp3 file");
        if(lastDir!=null){
            fc.setCurrentDirectory(lastDir);
        }
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            lastDir = fc.getCurrentDirectory();
            //This is where a real application would open the file.
            println("Opening: " + file.getName() + ".");
            if(!file.getAbsolutePath().contains(".mp3")){
                promptUserForSoundtrack();
            }else{
                ostFilepath = file.getAbsolutePath();
            }
        }else{
            // private String ostFilepath
        }
    }
}