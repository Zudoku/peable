/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author arska
 */
public class CameraController {
    private final Camera camera;
    
    public CameraController(Camera camera){
        this.camera=camera;
    }
    public void moveUp(){
        camera.setLocation(new Vector3f(camera.getLocation().x+0.4f, camera.getLocation().y, camera.getLocation().z+0.4f));
    }
    public void moveDown(){
        camera.setLocation(new Vector3f(camera.getLocation().x-0.4f, camera.getLocation().y, camera.getLocation().z-0.4f));
    }
    public void moveRight(){
        camera.setLocation(new Vector3f(camera.getLocation().x-0.4f, camera.getLocation().y, camera.getLocation().z+0.2f));
    }
    public void moveLeft(){
        camera.setLocation(new Vector3f(camera.getLocation().x+0.4f, camera.getLocation().y, camera.getLocation().z-0.2f));
    }
    
}
