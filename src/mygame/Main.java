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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.GUI.IngameHUD;
import mygame.GUI.SelectionParticleEmitter;
import mygame.GUI.StartScreen;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.UserInput;
import mygame.terrain.RoadMaker;
import mygame.terrain.WorldHandler;

/**
 * test
 * @author arska
 */
public class Main extends SimpleApplication {
    
    public static  WorldHandler worldHandler;
    
    public static ClickingHandler clickingHandler;
    public static RoadMaker roadMaker;
    IngameHUD ingameHUD;
    StartScreen startScreen;
    Nifty nifty;
    SelectionParticleEmitter selectionEmitter;
    
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        
    }
    private UserInput userInput;
    

    @Override
    public void simpleInitApp() {
        ingameHUD=new IngameHUD();
        startScreen=new StartScreen();
        
        
        userInput=new UserInput(rootNode,inputManager,cam);
        rootNode.setName("gamenode");
        worldHandler = new WorldHandler(rootNode,assetManager,ingameHUD);
        selectionEmitter=new SelectionParticleEmitter(assetManager, rootNode,worldHandler);
        clickingHandler=new ClickingHandler(worldHandler);
        roadMaker=new RoadMaker(assetManager, rootNode);
        worldHandler.makeGround();
         userInput.giveClickHandler(clickingHandler);
         ingameHUD.givefields(clickingHandler,worldHandler);
        Camera camera =getCamera();
        camera.setLocation(new Vector3f(-11.696763f, 30.377302f, -13.492211f));
        
        
        camera.setFrame(new Vector3f(-11.696763f, 30.377302f, -13.492211f),new Vector3f(0.2f,0,0),new Vector3f(0,0,0),new Vector3f(0.48968115f, -0.65352046f, 0.57716846f));
        camera.setRotation(new Quaternion(0.32836914f, 0.32047316f, -0.06872874f, 0.8858595f));
       
       flyCam.setEnabled(false);
       
       
      
       
       NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
    assetManager, inputManager, audioRenderer, guiViewPort);
    /** Create a new NiftyGUI object */
    nifty = niftyDisplay.getNifty();
    /** Read your XML and initialize your custom ScreenController */
    nifty.fromXml("Interface/Nifty/niftytest.xml", "start");
    
  
    // attach the Nifty display to the gui view port as a processor
    guiViewPort.addProcessor(niftyDisplay);
      selectionEmitter.initSelection();
       Spatial teapot = assetManager.loadModel("Models/roadBending.j3o");
        Material mat_default = new Material( 
            assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        teapot.scale(0.5f, 0.5f, 0.5f);
        teapot.move(20, 6.1f, 20);
        rootNode.attachChild(teapot);
        

        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        nifty.update();
        selectionEmitter.updateSelection(rootNode, inputManager, cam);
    }

    @Override
    public void simpleRender(RenderManager rm) {
      ;
    }
    
}
