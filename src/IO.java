import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IO {
    public static ArrayList<String> getFilenamesInDirectory(String dir){
        ArrayList<String> filenames = new ArrayList<String>();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                filenames.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                // :) saving this handy little check for future use
            }
        }
        return filenames;
    }
}
