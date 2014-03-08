package mygame;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import mygame.GUI.IngameHUD;
import mygame.GUI.StartScreen;
import mygame.gameplayorgans.Scenario;
import mygame.gameplayorgans.ScenarioManager;
import mygame.terrain.ParkHandler;

/**
 * test
 * @author arska
 */
public class Main extends SimpleApplication {
    //LOGGER
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    //DEPENDENCIES
    public static Nifty nifty;
    private static SettingsLoader settingsLoader;
    public static Injector injector;
    @Inject private UtilityMethods utilityMethods;
    @Inject public IngameHUD ingameHUD;
    @Inject LoadManager loadManager; 
    //OWNS
    @Inject public ParkHandler currentPark;
    private StartScreen startScreen;
    public static Settings intosettings;
    private static Gamestate gamestate;
    @Inject private ScenarioManager scenarioManager;
    //VARIABLES
    public static int startgame=0;
    public static Scenario scenario;

    public static void main(String[] args) {
        //Load custom settings from a binary file.
        settingsLoader=new SettingsLoader();
        intosettings=settingsLoader.load();
        //Start Main with appsettings from custom settings.
        Main app = new Main();
        app.setSettings(intosettings.getAppSettings());
        app.setShowSettings(false);
        app.start(); 
    }
    public boolean startDebug(){
        if(startgame==1){
            return true;
        }
        return false;
    }
    
    @Override
    public void simpleInitApp() {
        injector = Guice.createInjector(new GameModule(rootNode,assetManager,settings,cam,inputManager));//Init Guice
        injector.injectMembers(this);
        
        gamestate=new Gamestate(loadManager);
        startScreen=new StartScreen();
        //Initialize Nifty (GUI COMPONENT)
        initNifty();
        //Disable StatView
        setDisplayStatView(false);
        //Set cursor visible
        inputManager.setCursorVisible(true);
        //Disable fly camera
        flyCam.setEnabled(false);   
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        nifty.update();
        if(startgame==1){
            scenario=scenarioManager.greenGolly();
            startGame();
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
    
    
    /**
     * Nifty (GUI COMPONENT) needs to be initialized here.
     */
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

}
