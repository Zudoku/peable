/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.ArrayList;
import mygame.GUI.Button;

/**
 *
 * @author arska
 */
public class UserInput {

    InputManager inputManager;
    Camera cam;
    private final Node rootNode;
    private ClickingHandler clickingHandler;
    long lastclicked = 0;
    ArrayList<Button> buttons;
    ArrayList<Button> dragbuttons;

    public UserInput(Node rootNode, InputManager inputManager, Camera cam) {
        this.rootNode = rootNode;
        this.inputManager = inputManager;
        this.cam = cam;
        inputManager.setCursorVisible(true);
        inputManager.addMapping("mouseleftclick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("mouserightclick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("test", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(actionListener, "mouseleftclick", "mouserightclick");
        inputManager.addListener(analogListener, "test","mouseleftclick");
      
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {

            if (name.equals("mouseleftclick")) {
                if (System.currentTimeMillis() - lastclicked > 100) {
                    lastclicked = System.currentTimeMillis();
                    if(testClicks()==true){
                        return;
                    }
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
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if(name=="mouseleftclick"){
                if(testDragClicks()==true){
                    
                }
            }
        }
    };

    public void update() {
    }

    public void giveClickHandler(ClickingHandler clickingHandler,ArrayList<Button> buttons,ArrayList<Button> dragbuttons) {
        this.clickingHandler = clickingHandler;
        this.buttons=buttons;
        this.dragbuttons=dragbuttons;
    }
    public boolean testClicks(){
        for(Button b:buttons){
            if(b.testHit(inputManager.getCursorPosition().x, inputManager.getCursorPosition().y)==true){
                b.listener.oAction(b.getName());
                return true;
            }
        }
        return false;
        
    }
    public boolean testDragClicks(){
        for(Button b:dragbuttons){
            
        }
        
        return false;
    }
}
