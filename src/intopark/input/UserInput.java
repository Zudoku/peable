/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input;

import intopark.input.mouse.ClickingHandler;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class UserInput {
    private static final Logger logger = Logger.getLogger(UserInput.class.getName());
    //DEPENDENCIES
    private InputManager inputManager;
    private CameraController cameraController;
    private MyAnalogListener analogListener;
    private MyActionListener actionListener;
    //ALL KEYTRIGGERS (KEYMAPS) LOADED WITH DEFAULT KEYBINDS.
    //CAMERA
    KeyTrigger moveCameraUp = new KeyTrigger(KeyInput.KEY_W);
    KeyTrigger moveCameraDown = new KeyTrigger(KeyInput.KEY_S);
    KeyTrigger moveCameraRight = new KeyTrigger(KeyInput.KEY_D);
    KeyTrigger moveCameraLeft = new KeyTrigger(KeyInput.KEY_A);
    KeyTrigger rotateCameraRight=new KeyTrigger(KeyInput.KEY_E);
    KeyTrigger rotateCameraLeft=new KeyTrigger(KeyInput.KEY_Q);
    //ROTATE
    KeyTrigger rotateRight = new KeyTrigger(KeyInput.KEY_O);
    KeyTrigger rotateLeft = new KeyTrigger(KeyInput.KEY_P);
    //MOUSE
    MouseButtonTrigger mouseLeftClick = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    MouseButtonTrigger mouseRightClick = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
    MouseButtonTrigger mouseLeftDrag=new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    MouseButtonTrigger mouseRightDrag = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);

    @Inject
    public UserInput(Node rootNode, InputManager inputManager, Camera cam, ClickingHandler clickingHandler,EventBus eventBus) {
        this.inputManager = inputManager;
        cameraController = new CameraController(cam);
        initControls(); //Get players own controls customisation mappings
        inputManager.setCursorVisible(true); //Set cursor visible
        analogListener=new MyAnalogListener(cameraController,clickingHandler,inputManager); //Controls analog input
        actionListener=new MyActionListener(inputManager,clickingHandler,eventBus); //Controls action input (for example: 1 button press)
        //mouse input
        addTrigger("mouseleftclick", mouseLeftClick);
        addTrigger("mouserightclick", mouseRightClick);
        addTrigger("mouseleftdrag", mouseLeftDrag);
        addTrigger("mouserightdrag",mouseRightDrag);
        //ingame Actions
        addTrigger("rotateRight", rotateRight);
        addTrigger("rotateLeft", rotateLeft);
        //Camera input
        addTrigger("movecameraup", moveCameraUp);
        addTrigger("movecameradown", moveCameraDown);
        addTrigger("movecameraright", moveCameraRight);
        addTrigger("movecameraleft", moveCameraLeft);
        addTrigger("rotatecameraright", rotateCameraRight);
        addTrigger("rotatecameraleft", rotateCameraLeft);

        inputManager.addListener(analogListener,"mouseleftdrag","mouserightdrag","movecameraup","movecameradown","movecameraright", "movecameraleft","rotatecameraright","rotatecameraleft");
        inputManager.addListener(actionListener,"mouseleftclick","mouserightclick","rotateRight","rotateLeft");
    }
    /**
     * Shorter wrapper to add keybind maps.
     * @param name mapping name.
     * @param trigger
     */
    private void addTrigger(String name, Trigger trigger){
        inputManager.addMapping(name, trigger);
    }
    public void initControls(){
        //TODO:
    }

    public void update(){
        analogListener.checkDragging();
        analogListener.onCursorHover();
    }
    /**
     * Utility method to figure if Keybind is for right or left mouse.
     * @param control mapping name.
     * @return true if left.
     */
    public static boolean getLeftMouse(String control){
        if(control.contains("mouseleft")){
            return true;
        }else if(control.contains("mouseright")){
            return false;
        }
        throw new IllegalArgumentException();
    }

    public CameraController getCameraController() {
        return cameraController;
    }


}
