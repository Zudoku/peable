/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.app.state.AbstractAppState;
import intopark.Main;
import intopark.UtilityMethods;
import intopark.inout.events.LoadFileEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class StartScreen extends AbstractAppState {
    //LOGGER
    private static final Logger logger = Logger.getLogger(StartScreen.class.getName());
    //DEPENDENCIES
    @Inject EventBus eventBus;
    //VARIABLES
    public int startgame=0;
    private File[] savedGames;
    private File selectedFile;
    private int timesClicked=2;

    /**
     * NiftyGUI controller for the startup-UI.
     * Contains all the methods callable from UI.
     */
    public StartScreen(){

    }

    public void onStartScreen() {
        Main.injector.injectMembers(this); //bad habit.
    }


    public void onEndScreen() {

    }
    /**
     * Starts the real game.
     */
    public void startGameLoad(){
        eventBus.post(new LoadFileEvent(selectedFile));
        Main.startgame=5;
    }
    /**
     * This adds all the IntoFile files the the listbox on the startup UI.
     */
    public void populateSavedGames(){
        //Find the listbox
        //Find all the saves in the saves folder.
        File dir = new File("Saves");
    	savedGames= dir.listFiles(new FilenameFilter() {
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".IntoFile"); }
    	} );
        //Add them to the listbox
        for(File file:savedGames){
        }
    }
    
    public String getCurrentVersion(){
        return "Version "+ UtilityMethods.currentVersion+ "  ";
    }
}
