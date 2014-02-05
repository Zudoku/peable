/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.terrain.decoration.RotationEvent;

/**
 *
 * @author arska
 */
@Singleton
public class UserInput {
    private static final Logger logger = Logger.getLogger(UserInput.class.getName());
    InputManager inputManager;
    Camera cam;
    private final Node rootNode;
    private ClickingHandler clickingHandler;
    long lastclicked = 0;
    CameraController cameraController;
    KeyTrigger moveCameraUp = new KeyTrigger(KeyInput.KEY_W);
    KeyTrigger moveCameraDown = new KeyTrigger(KeyInput.KEY_S);
    KeyTrigger moveCameraRight = new KeyTrigger(KeyInput.KEY_D);
    KeyTrigger moveCameraLeft = new KeyTrigger(KeyInput.KEY_A);
    KeyTrigger rotateRight = new KeyTrigger(KeyInput.KEY_E);
    
    KeyTrigger rotateLeft = new KeyTrigger(KeyInput.KEY_Q);
    MouseButtonTrigger mouseLeftClick = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    MouseButtonTrigger mouseRightClick = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
    private final EventBus eventBus;

    @Inject
    public UserInput(Node rootNode, InputManager inputManager, Camera cam, ClickingHandler clickingHandler,EventBus eventBus) {
        this.rootNode = rootNode;
        this.inputManager = inputManager;
        this.cam = cam;
        this.clickingHandler = clickingHandler;
        this.eventBus=eventBus;
        cameraController = new CameraController(cam);
        inputManager.setCursorVisible(true);

        //mouse input
        inputManager.addMapping("mouseleftclick", mouseLeftClick);
        inputManager.addMapping("mouserightclick", mouseRightClick);
        //ingame Actions
        inputManager.addMapping("rotateRight", rotateRight);
        inputManager.addMapping("rotateLeft", rotateLeft);
        //Camera input
        inputManager.addMapping("movecameraup", moveCameraUp);
        inputManager.addMapping("movecameradown", moveCameraDown);
        inputManager.addMapping("movecameraright", moveCameraRight);
        inputManager.addMapping("movecameraleft", moveCameraLeft);
        

        inputManager.addListener(actionListener, "mouseleftclick", "mouserightclick", "rotateRight", "rotateLeft");

        inputManager.addListener(analogListener, "movecameraup", "movecameradown", "movecameraright", "movecameraleft");

    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {

            if (name.equals("mouseleftclick")) {
                if (System.currentTimeMillis() - lastclicked > 100) {
                    lastclicked = System.currentTimeMillis();

                    CollisionResults results = new CollisionResults();


                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.getX(), click2d.getY()), 0f);

                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.getX(), click2d.getY()), 1f).
                            subtractLocal(click3d);

                    Ray ray = new Ray(cam.getLocation(), dir);

                    rootNode.collideWith(ray, results);

                    if (results.size() > 0) {
                        CollisionResult target = results.getClosestCollision();
                        clickingHandler.handleClicking(target, results);

                    } else {
                        logger.log(Level.FINE,"You clicked to VOID !");
                    }
                }





            }
            if (name.equals("mouserightclick")) {
                /**TODO!
                 * if(isPressed){
                    return;
                }
                 */
                if (System.currentTimeMillis() - lastclicked > 100) {
                    lastclicked = System.currentTimeMillis();

                    CollisionResults results = new CollisionResults();


                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.getX(), click2d.getY()), 0f);

                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.getX(), click2d.getY()), 1f).
                            subtractLocal(click3d);

                    Ray ray = new Ray(cam.getLocation(), dir);

                    rootNode.collideWith(ray, results);

                    if (results.size() > 0) {
                        CollisionResult target = results.getClosestCollision();
                        clickingHandler.handleRightClicking(target, results);

                    }
                }
            }
            if (name.equals("rotateRight")) {
                if(isPressed){
                    return;
                }
                switch (clickingHandler.clickMode) {
                    case DECORATION:
                        eventBus.post(new RotationEvent(1,0));
                        break;

                    case PLACE:
                        eventBus.post(new RotationEvent(1,2));
                        break;

                    case ROAD:
                        eventBus.post(new RotationEvent(1,1));
                        break;


                }
            }
            if (name.equals("rotateLeft")) {
                if(isPressed){
                    return;
                }
                switch (clickingHandler.clickMode) {
                    case DECORATION:
                        eventBus.post(new RotationEvent(0,0));
                        break;

                    case PLACE:
                        eventBus.post(new RotationEvent(0,2));
                        break;

                    case ROAD:
                        eventBus.post(new RotationEvent(0,1));
                        break;


                }
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("movecameraup")) {
                cameraController.moveUp();
            }
            if (name.equals("movecameradown")) {
                cameraController.moveDown();
            }
            if (name.equals("movecameraright")) {
                cameraController.moveRight();
            }
            if (name.equals("movecameraleft")) {
                cameraController.moveLeft();
            }
        }
    };
}
