/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

import com.google.inject.Singleton;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class MyAnalogListener implements AnalogListener {
    private static final Logger logger = Logger.getLogger(MyAnalogListener.class.getName());
    private boolean isMouseDragging=false;
    private long lastDragged;
    private CameraController cameraController;
    private ClickingHandler clickingHandler;
    private InputManager inputManager;
    public MyAnalogListener(CameraController camcontrol, ClickingHandler clickH, InputManager inputManager) {
        this.cameraController=camcontrol;
        this.clickingHandler=clickH;
        this.inputManager=inputManager;
    }
    
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

        if (name.equals("mouseleftdrag")) {
            if (isMouseDragging) {
                lastDragged = System.currentTimeMillis();
                clickingHandler.handleMouseDrag(inputManager.getCursorPosition().y, lastDragged);
            } else {
                lastDragged = System.currentTimeMillis();
                isMouseDragging = true;
            }
        }
    }

    public void checkDragging() {
        if (isMouseDragging) {
            if (System.currentTimeMillis() - lastDragged > 200) {
                isMouseDragging = false;
                clickingHandler.handleMouseDragRelease();
            }
        }
    }
}
