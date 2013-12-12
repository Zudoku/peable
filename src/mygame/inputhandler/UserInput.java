/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

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


/**
 *
 * @author arska
 */
@Singleton
public class UserInput {

    InputManager inputManager;
    Camera cam;
    private final Node rootNode;
    private ClickingHandler clickingHandler;
    long lastclicked = 0;
    CameraController cameraController;
 

    @Inject
    public UserInput(Node rootNode, InputManager inputManager, Camera cam,ClickingHandler clickingHandler) {
        this.rootNode = rootNode;
        this.inputManager = inputManager;
        this.cam = cam;
        this.clickingHandler=clickingHandler;
        cameraController=new CameraController(cam);
        inputManager.setCursorVisible(true);
        inputManager.addMapping("mouseleftclick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("mouserightclick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("test", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("movecameraup", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("movecameradown", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("movecameraright", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("movecameraleft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(actionListener, "mouseleftclick", "mouserightclick");
        inputManager.addListener(analogListener,"movecameraup","movecameradown","movecameraright","movecameraleft" );
      
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
                        System.out.println("klikkasit huti");
                    }
                }





            }
            if (name.equals("mouserightclick")) {
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
        }
    };

    
    private AnalogListener analogListener = new AnalogListener() {
       
        public void onAnalog(String name, float value, float tpf) {
            if(name.equals("movecameraup")){
                cameraController.moveUp();
            }
            if(name.equals("movecameradown")){
                cameraController.moveDown();
            }
            if(name.equals("movecameraright")){
                cameraController.moveRight();
            }
            if(name.equals("movecameraleft")){
                cameraController.moveLeft();
            }
        }
    };


    
    
}
