package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.GUI.IngameHUD;
import mygame.GUI.StartScreen;
import mygame.terrain.ParkHandler;

/**
 * test
 * @author arska
 */
public class Main extends SimpleApplication {

    public static ParkHandler currentPark;
    StartScreen startScreen;
    public static Nifty nifty;
    public static Gamestate gamestate;
    public static int startgame=0;
    public static SaveManager saveManager;
    public LoadManager loadManager;
    public static void main(String[] args) {
        
        
        
        Main app = new Main();
        
        app.start();
        
    }
    public IngameHUD ingameHUD;
    
    @Override
    public void simpleInitApp() {
        gamestate=new Gamestate();
        
        
        loadManager=new LoadManager(rootNode,settings);
        saveManager=new SaveManager(loadManager);
        
        ingameHUD=new IngameHUD();
        startScreen=new StartScreen();
        currentPark=new ParkHandler(rootNode,settings);
        //nifty
        initNifty();
        setDisplayStatView(false);
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        //settings.setTitle("THEMEPARK TYCOON MADE BY ARTTU SIREN      please dont steal :");
    }

    @Override
    public void simpleUpdate(float tpf) {
        nifty.update();
        if(startgame==1){
            startGame();
        }
        if(startgame==5){
            startLoadGame();
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
      ;
    }
    public void startGame(){
        stateManager.attach(gamestate);
        gamestate.setEnabled(true);
        
    }
    public void startLoadGame(){
        try {
            loadManager.load("testfilexd");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    
}
