package intopark;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import intopark.inout.SettingsLoader;
import intopark.inout.Settings;
import intopark.inout.LoadManager;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.util.logging.Logger;
import intopark.GUI.StartScreen;
import intopark.inout.LoadFileEvent;
import intopark.terrain.ParkHandler;
import java.io.File;
import java.util.logging.Level;

/**
 * test
 * @author arska
 */
public class Main extends SimpleApplication {
    //LOGGER
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    //DEPENDENCIES
    private static SettingsLoader settingsLoader;
    public static Injector injector;
    @Inject private UtilityMethods utilityMethods;
    @Inject LoadManager loadManager; 
    @Inject EventBus eventBus;
    //OWNS
    @Inject public ParkHandler currentPark;
    private StartScreen startScreen;
    public static Settings intosettings;
    private static Gamestate gamestate;
    //VARIABLES
    public static int startgame=5;
    private File scenariofile;

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
        //Attach EventBus here so we can catch loadFileEvents
        eventBus.register(this);
        //Disable StatView
        setDisplayStatView(false);
        //Set cursor visible
        inputManager.setCursorVisible(true);
        //Disable fly camera
        flyCam.setEnabled(false);   
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(startgame==5){
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
    
    public Injector getInjector(){
        return injector;
    }
    @Subscribe
    public void listenLoadFileEvent(LoadFileEvent event){
        if(event.getFile()!=null){
            scenariofile=event.getFile();
            logger.log(Level.FINEST,"File {0} ready for deployment!",event.getFile().getName());
            return;
        }
        logger.log(Level.WARNING,"Failed to initialize file. Game might misbehave!");
    }

    public File getScenariofile() {
        return scenariofile;
    }
    
}
