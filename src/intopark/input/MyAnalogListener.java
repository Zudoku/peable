/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input;

import intopark.input.mouse.ClickingHandler;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import intopark.UtilityMethods;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class MyAnalogListener implements AnalogListener {
    private static final Logger logger = Logger.getLogger(MyAnalogListener.class.getName());
    //DEPENDENCIES
    private CameraController cameraController;
    private ClickingHandler clickingHandler;
    private InputManager inputManager;
    //VARIABLES
    private boolean isMouseDragging=false;
    private long lastDragged;
    private String dragger="";

    public MyAnalogListener(CameraController camcontrol, ClickingHandler clickH, InputManager inputManager) {
        this.cameraController=camcontrol;
        this.clickingHandler=clickH;
        this.inputManager=inputManager;
    }

    @Override
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
        if (name.equals("rotatecameraright") || name.equals("rotatecameraleft")) {
            if (name.equals("rotatecameraright")) {
                cameraController.onTurnCamera(true);
            } else {
                cameraController.onTurnCamera(false);
            }

        }

        if (name.equals("mouseleftdrag")||name.equals("mouserightdrag")) {
            if (isMouseDragging) {
                if(dragger.equals(name)){
                    lastDragged = System.currentTimeMillis();
                    CollisionResults results=new CollisionResults();
                    UtilityMethods.rayCast(results, null);
                    float x=inputManager.getCursorPosition().x;
                    float z=inputManager.getCursorPosition().y;
                    clickingHandler. handleMouseDrag(UserInput.getLeftMouse(dragger),x,z,results,lastDragged);
                }

            } else {
                lastDragged = System.currentTimeMillis();
                dragger=name;
                isMouseDragging = true;
            }
        }
    }

    public void checkDragging() {
        if (isMouseDragging) {
            if (System.currentTimeMillis() - lastDragged > 200) {
                isMouseDragging = false;
                CollisionResults results=new CollisionResults();
                UtilityMethods.rayCast(results, null);
                float x=inputManager.getCursorPosition().x;
                float z=inputManager.getCursorPosition().y;
                clickingHandler.handleMouseDragRelease(x,z,results);
            }
        }
    }
    public void onCursorHover(){
        float x=inputManager.getCursorPosition().x;
        float z=inputManager.getCursorPosition().y;
        CollisionResults results=new CollisionResults();
        UtilityMethods.rayCast(results, null);
        clickingHandler.handleCursorHover(x,z,results);
    }

}
