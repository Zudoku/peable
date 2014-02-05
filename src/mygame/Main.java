package mygame;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
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
    public static void main(String[] args) {
        
        
        
        Main app = new Main();
        
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
        currentPark=injector.getInstance(ParkHandler.class);
        //nifty
        initNifty();
        setDisplayStatView(false);
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        lightsOn();   
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
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2);
    }
    private Spatial findNode(Node rootNode, String name) {
        if (name.equals(rootNode.getName())) {
            return rootNode;
        }
        return rootNode.getChild(name);
    }
    
}
