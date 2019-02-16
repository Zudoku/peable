/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import intopark.Main;
import intopark.UtilityMethods;
import intopark.inout.LoadFileEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class StartScreen extends AbstractAppState {
    private static final Logger logger = Logger.getLogger(StartScreen.class.getName());

    @Inject EventBus eventBus;

    private File[] savedGames;
    private File selectedFile;

    public StartScreen(){

    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        
    }


    public void startGameLoad(){
        eventBus.post(new LoadFileEvent(selectedFile));
    }

    
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
