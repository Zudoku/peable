package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import de.lessvoid.nifty.Nifty;
import mygame.GUI.IngameHUD;
import mygame.GUI.SelectionParticleEmitter;
import mygame.GUI.StartScreen;
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
 * test
 * @author arska
 */
public class Main extends SimpleApplication {
    
    public static  TerrainHandler worldHandler;
    
    public static ClickingHandler clickingHandler;
    public static RoadMaker roadMaker;
    public static HolomodelDrawer holoDrawer;
    public static ShopManager shopManager;
    public static IngameHUD ingameHUD;
    public static ParkHandler currentPark;
    StartScreen startScreen;
    Nifty nifty;
    SelectionParticleEmitter selectionEmitter;
    public static NPCManager npcManager;
    public static WindowMaker windowMaker;
    private UserInput userInput;
    public static RideManager rideManager;
    
    public static void main(String[] args) {
        
        Main app = new Main();
        app.start();
        
    }
  
    @Override
    public void simpleInitApp() {
        //ei peliin kuuluvat
        ingameHUD=new IngameHUD();
        startScreen=new StartScreen();
        currentPark=new ParkHandler(rootNode,settings);
        userInput=new UserInput(rootNode,inputManager,cam);
        
        //peliin kuuluvat
        holoDrawer=new HolomodelDrawer(assetManager, rootNode);
        worldHandler = new TerrainHandler(rootNode,assetManager,ingameHUD);
        selectionEmitter=new SelectionParticleEmitter(assetManager, rootNode,worldHandler);
        clickingHandler=new ClickingHandler(worldHandler);
        roadMaker=new RoadMaker(assetManager, rootNode);
        
         userInput.giveClickHandler(clickingHandler);
         ingameHUD.givefields(clickingHandler,worldHandler);
         
        npcManager=new NPCManager(rootNode,assetManager);
        shopManager=new ShopManager(assetManager,rootNode);
        rideManager=new RideManager(assetManager, rootNode);
        selectionEmitter.initSelection();
        //lataa map DEBUG ONLY CHANGE LATER
        currentPark.loadDebugPlain();
        //laita kamera ok
        Camera camera =getCamera();
        camera.setLocation(new Vector3f(-11.696763f, 30.377302f, -13.492211f));
        camera.setFrame(new Vector3f(-11.696763f, 30.377302f, -13.492211f),new Vector3f(0.2f,0,0),new Vector3f(0,0,0),new Vector3f(0.48968115f, -0.65352046f, 0.57716846f));
        camera.setRotation(new Quaternion(0.32836914f, 0.32047316f, -0.06872874f, 0.8858595f));
       
        //disable kamera
        flyCam.setEnabled(false);
       
        //nifty
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        assetManager, inputManager, audioRenderer, guiViewPort);
        /** Create a new NiftyGUI object */
        nifty = niftyDisplay.getNifty();
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml("Interface/Nifty/niftytest.xml", "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        //valo
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2); 
        
        windowMaker=new WindowMaker(nifty);
        setDisplayStatView(false);
        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        nifty.update();
        selectionEmitter.updateSelection(rootNode, inputManager, cam);
        npcManager.update();
        rideManager.updateRideQueues();
    }

    @Override
    public void simpleRender(RenderManager rm) {
      ;
    }
    public int getMoneyslotX(){
        int p=settings.getWidth()-300;
        return p;
    }
    
    
}
