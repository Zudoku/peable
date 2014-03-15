/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import de.lessvoid.nifty.Nifty;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.IngameHUD;
import intopark.GUI.SelectionParticleEmitter;
import intopark.gameplayorgans.Scenario;
import intopark.inputhandler.UserInput;
import intopark.npc.NPCManager;
import intopark.ride.RideManager;
import intopark.terrain.MapFactory;
import intopark.terrain.ParkHandler;


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
    @Inject private MapFactory mapFactory;
    //VARIABLES
    private Scenario scenario;
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
            super.initialize(stateManager, app);
            this.appm = (Main) app;
            this.nifty=this.appm.getNifty();
            appm.getInjector().injectMembers(this); //Inject members 
            currentPark=this.appm.currentPark;
            ingameHUD=this.appm.ingameHUD;
            logger.log(Level.FINEST, "Loading scenario.");
        
            
        try {
            loadManager.load("testfilexd.IntoFile");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Gamestate.class.getName()).log(Level.SEVERE, "EROR", ex);
        } catch (IOException ex) {
            Logger.getLogger(Gamestate.class.getName()).log(Level.SEVERE, "EROR", ex);
        }
              //  mapFactory.setCurrentMapPlain();
            
            setCamera();

            this.appm.setDisplayStatView(false);

            this.appm.setPauseOnLostFocus(false);
            currentPark.onStartup(); 
            logger.log(Level.FINEST, "Gamestate initialized.");
        
    }

    public int getMoneyslotX() {
        int p = this.appm.getContext().getSettings().getWidth() - 300;
        return p;
    }

    

    private void setCamera() {
        Camera camera = appm.getCamera();
        camera.setLocation(new Vector3f(-11.696763f, 30.377302f, -13.492211f));
        camera.setFrame(new Vector3f(-11.696763f, 30.377302f, -13.492211f), new Vector3f(0.2f, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0.48968115f, -0.65352046f, 0.57716846f));
        camera.setRotation(new Quaternion(0.32836914f, 0.32047316f, -0.06872874f, 0.8858595f));
        
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
    /**
     * Note that update is only called while the state is both attached and enabled.
     * @param tpf time per frame in milliseconds 
     */
    @Override
    public void update(float tpf) {
        try{
        selectionEmitter.updateSelection();
        npcManager.update();
        rideManager.updateRide();
        userInput.update();
        }catch(Throwable t){
            logger.log(Level.SEVERE,"OH SHIT",t);
            t.printStackTrace();
        }
    }
    /**
     * Set the Scenario.
     * @param scenario Scenario to load.
     */
    public void setScenario(Scenario scenario){
        this.scenario=scenario;
    }
}
