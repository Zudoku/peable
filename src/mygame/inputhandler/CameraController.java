/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class CameraController {
    private static final Logger logger = Logger.getLogger(CameraController.class.getName());
    private final Camera camera;
    private static float MOVESPEED=0.3f;
    private float cameraHeight=20;
    private float radius=10;
    private double alpha=0;
    private float cameraCenterX=0;
    private float cameraCenterZ=0;
    private float cameraCenterY=6;
    
    public CameraController(Camera camera){
        this.camera=camera;
        
    }
    public void onTurnCamera(boolean right){

        turnCamera(right);
        camera.setLocation(nextCameraPosition());
        camera.lookAt(new Vector3f(cameraCenterX, cameraCenterY,cameraCenterZ), new Vector3f(0, 1, 0));
    }
    private void turnCamera(boolean right){
        if(right){
            alpha+=0.01;
            if(alpha>=2*Math.PI){
                alpha=0;
            }
        }else{
            alpha-=0.01;
            if(alpha<0){
                alpha=2*Math.PI;
            }
        }
    }
    private Vector3f nextCameraPosition(){
        
        double z=Math.sin(alpha);
        double x=Math.cos(alpha);
      
        x*=radius;
        z*=radius;
        
        x+=cameraCenterX;
        z+=cameraCenterZ;
        
        return new Vector3f((float)x,cameraHeight,(float)z);
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
