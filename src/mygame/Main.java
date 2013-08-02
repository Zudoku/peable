package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import mygame.GUI.Niftytest;
import mygame.GUI.UiHandler;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.UserInput;
import mygame.terrain.WorldHandler;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    WorldHandler worldHandler;
    private UserInput userInput;
    ClickingHandler clickingHandler;
    UiHandler uiMaker;
    
    
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        
    }
    

    @Override
    public void simpleInitApp() {
        //new Niftytest(assetManager,inputManager,audioRenderer,guiViewPort);
        uiMaker=new UiHandler(settings,assetManager,guiNode,inputManager);
            
        
        userInput=new UserInput(rootNode,inputManager,cam);
        rootNode.setName("gamenode");
        worldHandler = new WorldHandler(rootNode,assetManager);
        clickingHandler=new ClickingHandler(worldHandler);
        worldHandler.makeGround();
        
        Camera camera =getCamera();
        camera.setLocation(new Vector3f(100, 100, 50));
        
        camera.setRotation(new Quaternion(0.3f, -0.6f, 0.3f, 0.62f));
        
       flyCam.setMoveSpeed(100);
       flyCam.setDragToRotate(true);
       userInput.giveClickHandler(clickingHandler,uiMaker.getButtonList(),uiMaker.getDragButtonList());
       uiMaker.makeButtons();
       
       
              
        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
       
    }
    
}
