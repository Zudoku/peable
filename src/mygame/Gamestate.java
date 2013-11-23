/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import mygame.GUI.IngameHUD;
import mygame.GUI.SelectionParticleEmitter;
import mygame.GUI.WindowMaker;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.UserInput;
import mygame.npc.NPCManager;
import mygame.ride.RideManager;
import mygame.shops.HolomodelDrawer;
import mygame.shops.ShopManager;
import mygame.terrain.ParkHandler;
import mygame.terrain.RoadMaker;
import mygame.terrain.TerrainHandler;

/**
 *
 * @author arska
 */
public class Gamestate extends AbstractAppState {

    private Main app;
    public static TerrainHandler worldHandler;
    public static ClickingHandler clickingHandler;
    public static RoadMaker roadMaker;
    public static HolomodelDrawer holoDrawer;
    public static ShopManager shopManager;
    public static IngameHUD ingameHUD;
    public static ParkHandler currentPark;
    public static Nifty nifty;
    SelectionParticleEmitter selectionEmitter;
    public static NPCManager npcManager;
    public static WindowMaker windowMaker;
    private UserInput userInput;
    public static RideManager rideManager;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Camera cam;
    private FlyByCamera flyCam;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();
        this.flyCam = this.app.getFlyByCamera();
        this.nifty=this.app.getNifty();
        
        currentPark=this.app.currentPark;
        userInput = new UserInput(rootNode, inputManager, cam);
        ingameHUD=this.app.ingameHUD;
        //peliin kuuluvat
        holoDrawer = new HolomodelDrawer(assetManager, rootNode);
        worldHandler = new TerrainHandler(rootNode, assetManager, ingameHUD);
        selectionEmitter = new SelectionParticleEmitter(assetManager, rootNode, worldHandler);
        clickingHandler = new ClickingHandler(worldHandler);
        roadMaker = new RoadMaker(assetManager, rootNode);

        userInput.giveClickHandler(clickingHandler);
        ingameHUD.givefields(clickingHandler, worldHandler);

        npcManager = new NPCManager(rootNode, assetManager);
        shopManager = new ShopManager(assetManager, rootNode);
        rideManager = new RideManager(assetManager, rootNode);
        selectionEmitter.initSelection();
        //lataa map DEBUG ONLY CHANGE LATER
        currentPark.loadDebugPlain();
        //laita kamera ok
        setCamera();
        //valo
        lightsOn();

        windowMaker = new WindowMaker(nifty);
        this.app.setDisplayStatView(false);

        this.app.setPauseOnLostFocus(false);
    }

    public int getMoneyslotX() {
        int p = this.app.getContext().getSettings().getWidth() - 300;
        return p;
    }

    private void lightsOn() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2);
    }

    private void setCamera() {
        Camera camera = app.getCamera();
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

    // Note that update is only called while the state is both attached and enabled.
    @Override
    public void update(float tpf) {
        selectionEmitter.updateSelection(rootNode, inputManager, cam);
        npcManager.update();
        rideManager.updateRideQueues();
    }
}
