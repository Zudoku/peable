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
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.IngameHUD;
import intopark.GUI.SelectionParticleEmitter;
import intopark.inputhandler.UserInput;
import intopark.npc.NPCManager;
import intopark.ride.RideManager;
import intopark.roads.RoadManager;
import intopark.terrain.ParkHandler;
import java.io.File;


/**
 *
 * @author arska
 */
@Singleton
public class Gamestate extends AbstractAppState {
    //LOGGER
    private static final Logger logger = Logger.getLogger(Gamestate.class.getName());
    //DEPENDENCIES
    public static IngameHUD ingameHUD;
    private  ParkHandler currentPark;
    public  Nifty nifty;
    private Main appm;
    @Inject private final LoadManager loadManager;
    @Inject private SelectionParticleEmitter selectionEmitter;
    @Inject private NPCManager npcManager;
    @Inject private UserInput userInput;
    @Inject private  RideManager rideManager;
    @Inject private RoadManager roadMaker;

    //VARIABLES
    /**
     * This is state where the game is running.
     * @param loadManager to load the Scenario from file.
     */
    
    public Gamestate(LoadManager loadManager){
        this.loadManager=loadManager;
    }
    /**
     * This is called before the state goes live. Initializes some critical data.
     * @param stateManager This is provided by AppState.
     * @param app This is provided by AppState.
     */
     
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        logger.log(Level.FINEST, "Initializing Gamestate...");
        super.initialize(stateManager, app); //Init some JME stuff 
        //TODO:
        this.appm = (Main) app;
        this.nifty = this.appm.getNifty();
        appm.getInjector().injectMembers(this); //Inject members 
        currentPark = this.appm.currentPark;
        ingameHUD = this.appm.ingameHUD;
        //TODO ENDS
        logger.log(Level.FINEST, "Loading file.");
        loadScenario();
        userInput.getCameraController().initialize(); //Initialize our camera
        currentPark.onStartup(); //Finish initializing
        this.appm.setDisplayStatView(false); //Disable statview (DEBUG info)
        this.appm.setPauseOnLostFocus(false); //Keep running when not focused 
        logger.log(Level.FINEST, "Gamestate initialized.");

    }

    public int getMoneyslotX() {
        int p = this.appm.getContext().getSettings().getWidth() - 300;
        return p;
    }

    /**
     * JME call TODO: when doing exiting and loading scenarios again
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }
    /**
     * JME call TODO: if needed something here.
     * @param enabled If this state is enabled or not
     */
    @Override
    public void setEnabled(boolean enabled) {
        // Pause and unpause
        super.setEnabled(enabled);

    }
    /**
     * Note that update is only called while the state is both attached and enabled.
     * @param tpf time per frame in milliseconds 
     */
    @Override
    public void update(float tpf) {
        try{
        selectionEmitter.updateSelection();
        npcManager.update();
        rideManager.updateRides();
        userInput.update();
        roadMaker.update();
        }catch(Throwable t){
            logger.log(Level.SEVERE,"OH SHIT",t);
            t.printStackTrace();
        }
    }
    private void loadScenario(){
        File file=appm.getScenariofile();
        if(file!=null){
            loadManager.load("Saves/"+file.getName());
            logger.log(Level.FINEST,"Load is supposed to go as planned!");
        }else{
            loadManager.load("testfilexd.IntoFile");
            logger.log(Level.FINEST,"LoadFileEvent wasn't sent on time! Loading default scenario({0}) instead.","testfilexd.IntoFile");
        }
    }
}
