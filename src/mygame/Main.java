package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.GUI.IngameHUD;
import mygame.GUI.StartScreen;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.UserInput;
import mygame.terrain.WorldHandler;

/**
 * test
 * @author arska
 */
public class Main extends SimpleApplication {
    
    WorldHandler worldHandler;
    
    ClickingHandler clickingHandler;
    IngameHUD ingameHUD;
    StartScreen startScreen;
    
    
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        
    }
    

    @Override
    public void simpleInitApp() {
        ingameHUD=new IngameHUD();
        startScreen=new StartScreen();
        
        
        
        rootNode.setName("gamenode");
        worldHandler = new WorldHandler(rootNode,assetManager);
        clickingHandler=new ClickingHandler(worldHandler);
        worldHandler.makeGround();
        
        Camera camera =getCamera();
        camera.setLocation(new Vector3f(100, 100, 50));
        
        camera.setRotation(new Quaternion(0.3f, -0.6f, 0.3f, 0.62f));
        
       flyCam.setMoveSpeed(100);
       flyCam.setDragToRotate(true);
       
      
       
       NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
    assetManager, inputManager, audioRenderer, guiViewPort);
    /** Create a new NiftyGUI object */
    Nifty nifty = niftyDisplay.getNifty();
    /** Read your XML and initialize your custom ScreenController */
    nifty.fromXml("Interface/Nifty/niftytest.xml", "start");
    
    // attach the Nifty display to the gui view port as a processor
    guiViewPort.addProcessor(niftyDisplay);
       
              
        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
       
    }
    
}
