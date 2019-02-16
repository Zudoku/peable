/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

import intopark.inout.LoadManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.input.UserInput;
import intopark.npc.NPCManager;
import intopark.roads.RoadManager;
import intopark.terrain.ParkHandler;
import java.io.File;


/**
 *
 * @author arska
 */
@Singleton
public class Gamestate extends AbstractAppState {
    private static final Logger logger = Logger.getLogger(Gamestate.class.getName());
    
    private  ParkHandler currentPark;
    private Main appm;
    @Inject private final LoadManager loadManager;
    @Inject private NPCManager npcManager;
    @Inject private UserInput userInput;
    @Inject private RoadManager roadMaker;


    public Gamestate(LoadManager loadManager){
        this.loadManager=loadManager;
    }

    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        logger.log(Level.FINEST, "Initializing Gamestate...");
        super.initialize(stateManager, app);
        //TODO:
        this.appm = (Main) app;
        appm.getInjector().injectMembers(this);
        currentPark = this.appm.currentPark;
        //TODO ENDS
        logger.log(Level.FINEST, "Loading file.");
        loadScenario();
        userInput.getCameraController().initialize(); 
        currentPark.onStartup(); 
        this.appm.setDisplayStatView(false); 
        this.appm.setPauseOnLostFocus(false);
        logger.log(Level.FINEST, "Gamestate initialized.");

    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Pause and unpause
        super.setEnabled(enabled);

    }

    @Override
    public void update(float tpf) {
        try{
            npcManager.update();
            userInput.update();
            roadMaker.update();
        } catch(Throwable t) {
            logger.log(Level.SEVERE,"OH SHIT",t);
            t.printStackTrace();
        }
    }
    private void loadScenario(){
        File file=appm.getScenariofile();
        if (file!=null){
            loadManager.load("Saves/"+file.getName());
            logger.log(Level.FINEST,"Load is supposed to go as planned!");
        } else {
            loadManager.load("Scenarios/BLANKPARK.IntoFile");
            logger.log(Level.FINEST,"LoadFileEvent wasn't sent on time! Loading default scenario({0}) instead.","testfilexd.IntoFile");
        }
    }
}
