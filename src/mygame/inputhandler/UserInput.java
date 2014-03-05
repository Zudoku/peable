/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

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
    InputManager inputManager;
    
    
    CameraController cameraController;
    
    private MyAnalogListener analogListener;
    private MyActionListener actionListener;
    
    KeyTrigger moveCameraUp = new KeyTrigger(KeyInput.KEY_W);
    KeyTrigger moveCameraDown = new KeyTrigger(KeyInput.KEY_S);
    KeyTrigger moveCameraRight = new KeyTrigger(KeyInput.KEY_D);
    KeyTrigger moveCameraLeft = new KeyTrigger(KeyInput.KEY_A);
    KeyTrigger rotateCameraRight=new KeyTrigger(KeyInput.KEY_Q);
    KeyTrigger rotateCameraLeft=new KeyTrigger(KeyInput.KEY_E);
    
    KeyTrigger rotateRight = new KeyTrigger(KeyInput.KEY_O);
    KeyTrigger rotateLeft = new KeyTrigger(KeyInput.KEY_P);
    MouseButtonTrigger mouseLeftClick = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    MouseButtonTrigger mouseRightClick = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
    MouseButtonTrigger mouseLeftDrag=new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

    @Inject
    public UserInput(Node rootNode, InputManager inputManager, Camera cam, ClickingHandler clickingHandler,EventBus eventBus) {
        this.inputManager = inputManager;
        cameraController = new CameraController(cam);
        initControls(); //Get players own controls customisation mappings
        inputManager.setCursorVisible(true); //Set cursor visible
        analogListener=new MyAnalogListener(cameraController,clickingHandler,inputManager); //Controls analog input
        actionListener=new MyActionListener(rootNode,clickingHandler,eventBus); //Controls action input (for example: 1 button press)
        //mouse input
        addTrigger("mouseleftclick", mouseLeftClick);
        addTrigger("mouserightclick", mouseRightClick);
        addTrigger("mouseleftdrag", mouseLeftDrag);
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
        
        inputManager.addListener(analogListener,"mouseleftdrag","movecameraup","movecameradown","movecameraright", "movecameraleft","rotatecameraright","rotatecameraleft");
        inputManager.addListener(actionListener,"mouseleftclick","mouserightclick","rotateRight","rotateLeft");

        

    }
    private void addTrigger(String name, Trigger trigger){
        inputManager.addMapping(name, trigger);
    }
    public void initControls(){
        
    }
    public void update(){
        analogListener.checkDragging();
    }
  
}
