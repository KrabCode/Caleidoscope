import Drawables.Abstract.Drawable;
import Managers.Abstract.Manager;
import Managers.SoundSpectrumViewerManager;
import Managers.WaveManager;
import Sound.SoundAnalysis;
import Sound.SoundManager;
import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import processing.core.PImage;
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

    //////////////////////////////   ON AIR
    private boolean rec             = false;
    private boolean playSong        = true;

    //////////////////////////////

    private boolean fullScreen = false;
    private int windowWidth = 800;
    private int windowHeight = 800;

    private boolean pause = false;

    Recorder recorder;

    private boolean fadeout = false;
    private float fadeMag = 0;
    private float fadeSpd = 3f;
    private float fadeMin = 0;

    private SoundManager sm;
    private float peakThreshold = .2f;

    //IF THIS IS FALSE
    private boolean promptUserForSoundtrack = false;
    private final JFileChooser fc = new JFileChooser();
    public static List<File> userSelectedSoundtrack;
    //FILL OUT THIS
    public static String programmerSelectedFile ="C:\\Users\\Jakub\\Desktop\\Clutter\\Roadtrip\\Psychedelic\\07 · kyuss · space cadet.mp3";

    private List<PImage> imageStore;
    private String imageDirectory = null; //"C:\\Users\\Jakub\\Desktop\\196758-alerts\\png";

    private boolean flagA = true;
    private boolean flagB = true;
    private boolean flagC = true;





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
            userSelectedSoundtrack = promptUserForSoundtrack();
        }else{
            userSelectedSoundtrack = new ArrayList<File>(){};
            userSelectedSoundtrack.add(new File(programmerSelectedFile));
        }

        //instantiate stuff
        drawablesShown = new ArrayList<Drawable>();
        drawableFactories = new ArrayList<Manager>();

        sm = new SoundManager(this, userSelectedSoundtrack, playSong, peakThreshold);

        if(imageDirectory!=null && !imageDirectory.equals("")){
            imageStore = loadImagesFromDisk(imageDirectory);
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
                recorder.saveFrame();
            }
        }
        bgColor = 0;
    }

    WaveManager wm;

    /**
     * The scripts
     */
    private void tryAddNewFactories(){
        if(flagA){
            wm = new WaveManager(this);
            drawableFactories.add(wm);
            flagA = false;
        }
        if(flagB){
            int [] markedFrequencies = wm.getPointsOfInterest(sm.getFreshAnalysis().getSpectrum().length);

            for(int i = 0; i < markedFrequencies.length; i++){
                println("monitoring freq " + i + ": " + markedFrequencies[i]);
            }

            drawableFactories.add(new SoundSpectrumViewerManager(
                    this,
                    //put the viewport into the lower right corner
                    new Point(0, height-100),
                    new Point(width, height),
                    markedFrequencies
                    ));
            flagB = false;
        }
        if(flagC && Timekeeper.getTimer().getMsElapsed() > -1){
            //drawableFactories.add(new BandManager(this, 0, 50, imageStore));
            flagC = false;
        }
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

    private ArrayList<PImage> loadImagesFromDisk(String imgSourceDir){
        ArrayList<PImage> images = new ArrayList<PImage>();
        List<String> imgFilenames = IO.getFilenamesInDirectory(imgSourceDir);
        for(String s : imgFilenames){
            images.add(loadImage(imgSourceDir + "\\" + s));
        }
        return images;
    }



    public List<File> promptUserForSoundtrack() {
        //Handle open button action.
        List<File> songFiles = new ArrayList<File>();
        fc.setDialogTitle("Choose one or more .mp3 files");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        File lastDir = new File(System.getProperty("user.home") + "Music");
        if(lastDir!=null){
            fc.setCurrentDirectory(lastDir);
        }else {
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        }
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selection = (fc.getSelectedFiles());
            for (File file : selection) {
                songFiles.add(file);
            }
            if(songFiles.isEmpty()){
                promptUserForSoundtrack();
                //recursive nagging - gotta be a bit relentless
                //user clicked on APPROVE_OPTION and there's no files selected
                //like wtf dude, shape up or ship out
            }
        }else{
            playSong = false;
        }
        return songFiles;
    }


}