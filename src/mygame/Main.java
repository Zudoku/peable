package mygame;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import mygame.GUI.IngameHUD;
import mygame.GUI.StartScreen;
import mygame.terrain.ParkHandler;

/**
 * test
 * @author arska
 */
public class Main extends SimpleApplication {

    public ParkHandler currentPark;
    StartScreen startScreen;
    public static Nifty nifty;
    public static Gamestate gamestate;
    public static int startgame=0;
    public SaveManager saveManager;
    public LoadManager loadManager;
    public static Injector injector;
    public static UtilityMethods utilityMethods;
    public static Settings intosettings;
    private static SettingsLoader settingsLoader;
    public static void main(String[] args) {
        
        settingsLoader=new SettingsLoader();
        intosettings=settingsLoader.load();
        
        Main app = new Main();
        app.setSettings(intosettings.getAppSettings());
        app.setShowSettings(false);
        LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINEST); 
        app.start(); 
    }
    public IngameHUD ingameHUD;
    public boolean startDebug(){
        if(startgame==1){
            return true;
        }
        return false;
    }
    
    @Override
    public void simpleInitApp() {
        injector = Guice.createInjector(new GameModule(rootNode,assetManager,settings,cam,inputManager));
        loadManager=injector.getInstance(LoadManager.class);
        saveManager=injector.getInstance(SaveManager.class);
        gamestate=injector.getInstance(Gamestate.class);
        ingameHUD=injector.getInstance(IngameHUD.class);
        startScreen=new StartScreen();
        utilityMethods=injector.getInstance(UtilityMethods.class);
        currentPark=injector.getInstance(ParkHandler.class);
        //nifty
        initNifty();
        setDisplayStatView(false);
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        lightsOn();   
        test();
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        nifty.update();
        if(startgame==1){
            startGame();
        }
        if(startgame==5){
            startLoadGame();
            startgame=0;
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
    
    public void startGame(){
        stateManager.attach(gamestate);
        gamestate.setEnabled(true);
        
    }
    public void startLoadGame(){
        
        stateManager.attach(gamestate);
        gamestate.setEnabled(true);
        
    }
    
    
    private void initNifty(){
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        assetManager, inputManager, audioRenderer, guiViewPort);
        /** Create a new NiftyGUI object */
        nifty = niftyDisplay.getNifty();
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml("Interface/Nifty/niftytest.xml", "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
    }

    public Nifty getNifty() {
        return nifty;
    }
    public Injector getInjector(){
        return injector;
    }
    private void lightsOn() {
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f).normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(-0.5f, 0.5f, -0.5f).normalizeLocal()));
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2); 
        
        AmbientLight l=new AmbientLight();
        l.setColor(new ColorRGBA(1,1,1,300));
        
        rootNode.addLight(l);
    }
     public Geometry test(){
         Geometry blue;
         Box box1 = new Box(1,1,1);
        blue = new Geometry("Box222", box1);
        blue.setLocalTranslation(new Vector3f(1,6,1));
        Material mat1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        blue.setMaterial(mat1);
        rootNode.attachChild(blue);
        return blue;
    }
}
