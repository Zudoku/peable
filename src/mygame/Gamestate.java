/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.inject.Inject;
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
import mygame.GUI.IngameHUD;
import mygame.GUI.SelectionParticleEmitter;
import mygame.GUI.WindowMaker;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.UserInput;
import mygame.npc.NPCManager;
import mygame.ride.RideManager;
import mygame.shops.HolomodelDrawer;
import mygame.shops.ShopManager;
import mygame.terrain.MapFactory;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadMaker;
import mygame.terrain.TerrainHandler;

/**
 *
 * @author arska
 */
public class Gamestate extends AbstractAppState {

    private Main appm;
    public  TerrainHandler worldHandler;
    public  ClickingHandler clickingHandler;
    public  RoadMaker roadMaker;
    public  HolomodelDrawer holoDrawer;
    public  ShopManager shopManager;
    public static IngameHUD ingameHUD;
    public  ParkHandler currentPark;
    public  Nifty nifty;
    SelectionParticleEmitter selectionEmitter;
    public  NPCManager npcManager;
    public  WindowMaker windowMaker;
    private UserInput userInput;
    public  RideManager rideManager;

    private final LoadManager loadManager;
    private MapFactory mapFactory;
    @Inject
    public Gamestate(LoadManager loadManager){
        this.loadManager=loadManager;
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.appm = (Main) app;
        this.nifty=this.appm.getNifty();
        
        
        
        currentPark=this.appm.currentPark;
        userInput =appm.getInjector().getInstance(UserInput.class);
        ingameHUD=this.appm.ingameHUD;
        //peliin kuuluvat
        holoDrawer = appm.getInjector().getInstance(HolomodelDrawer.class);
        worldHandler = appm.getInjector().getInstance(TerrainHandler.class);
        selectionEmitter =appm.getInjector().getInstance(SelectionParticleEmitter.class);
        clickingHandler =appm.getInjector().getInstance(ClickingHandler.class);
        roadMaker =appm.getInjector().getInstance(RoadMaker.class);
        npcManager =appm.getInjector().getInstance(NPCManager.class);
        shopManager =appm.getInjector().getInstance(ShopManager.class);
        rideManager =appm.getInjector().getInstance(RideManager.class);
        mapFactory=appm.getInjector().getInstance(MapFactory.class);
        selectionEmitter.initSelection();
        //lataa map DEBUG ONLY CHANGE LATER
        if(appm.startDebug()){
            
            mapFactory.setCurrentMapPlain();
        }else{
            
        try {
            loadManager.load("testfilexd");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
        //laita kamera ok
        setCamera();

        

        
        this.appm.setDisplayStatView(false);

        this.appm.setPauseOnLostFocus(false);
        currentPark.onStartup();
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
        System.out.println(camera.getRotation());
        
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

    // Note that update is only called while the state is both attached and enabled.
    @Override
    public void update(float tpf) {
        selectionEmitter.updateSelection();
        npcManager.update();
        //System.out.println(cam.getRotation()+"   "+cam.getLocation());
        rideManager.updateRide();
    }
    
}
