/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author arska
 */
@Singleton
public class CameraController {
    private final Camera camera;
    private static float MOVESPEED=0.3f;
    
    public CameraController(Camera camera){
        this.camera=camera;
    }
    public void moveUp(){
        camera.setLocation(new Vector3f(camera.getLocation().x+MOVESPEED, camera.getLocation().y, camera.getLocation().z+MOVESPEED));
    }
    public void moveDown(){
        camera.setLocation(new Vector3f(camera.getLocation().x-MOVESPEED, camera.getLocation().y, camera.getLocation().z-MOVESPEED));
    }
    public void moveRight(){
        camera.setLocation(new Vector3f(camera.getLocation().x-MOVESPEED, camera.getLocation().y, camera.getLocation().z+MOVESPEED));
    }
    public void moveLeft(){
        camera.setLocation(new Vector3f(camera.getLocation().x+MOVESPEED, camera.getLocation().y, camera.getLocation().z-MOVESPEED));
    }
    
}
