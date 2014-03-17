/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

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
    //LOGGER
    private static final Logger logger = Logger.getLogger(CameraController.class.getName());
    //DEPENDENCIES
    private final Camera camera;
    //VARIABLES
    private static float MOVESPEED=0.15f; // Speed that the camera moves
    private static double ROTATESPEED=0.02; //Speed that te camera rotates
    private float cameraHeight=30; // Height where the camera is 
    private float radius=20; //Circe radius which the camera rotates around 
    private double alpha=0; // Alpha angle in radians
    private float cameraCenterX=0; //Camera offset X - Also the coordinate which the camera looks at.
    private float cameraCenterZ=5; //Camera offset Z - Also the coordinate which the camera looks at.
    private float cameraCenterY=6; //Camera offset Y - Also the coordinate which the camera looks at.
    
    public CameraController(Camera camera){
        this.camera=camera;
        refreshCamera();
    }
    public void onTurnCamera(boolean right){

        turnCamera(right);
        refreshCamera();
    }
    private void refreshCamera(){
        checkBoundaries(new Vector3f(cameraCenterX,cameraCenterY,cameraCenterZ));
        camera.setLocation(nextCameraPosition());
        camera.lookAt(new Vector3f(cameraCenterX, cameraCenterY,cameraCenterZ), new Vector3f(0, 1, 0));
    }
    private void checkBoundaries(Vector3f center){
        if(center.x<-5){
            cameraCenterX=-5;
        }else if(center.x>133){ //128 + 5
            cameraCenterX=133;
        }
        
        if(center.z<-5){
            cameraCenterZ=-5;
        }else if(center.z>133){
            cameraCenterZ=133;
        }
    }
    private void turnCamera(boolean right){
        if(right){
            alpha+=ROTATESPEED;
            if(alpha>=2*Math.PI){
                alpha=0;
            }
        }else{
            alpha-=ROTATESPEED;
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
        Vector3f asd=camera.getDirection();
        asd.y=0;
        Vector3f newpos=asd.normalizeLocal().mult(MOVESPEED);
        cameraCenterX+=newpos.x;
        cameraCenterZ+=newpos.z;
        refreshCamera();
    }
    public void moveDown(){
         Vector3f asd=camera.getDirection();
        asd.y=0;
        Vector3f newpos=asd.normalizeLocal().mult(-MOVESPEED);
        cameraCenterX+=newpos.x;
        cameraCenterZ+=newpos.z;
        refreshCamera();
    }
    public void moveRight(){
        Vector3f asd=camera.getDirection();
        asd.y=0;
        Vector3f newpos=asd.normalizeLocal().cross(new Vector3f(0,1,0)).normalizeLocal().mult(MOVESPEED);
        cameraCenterX+=newpos.x;
        cameraCenterZ+=newpos.z;
        refreshCamera();
    }
    public void moveLeft(){
        Vector3f asd=camera.getDirection();
        asd.y=0;
        Vector3f newpos=asd.normalizeLocal().cross(new Vector3f(0,1,0)).normalizeLocal().mult(-MOVESPEED);
        cameraCenterX+=newpos.x;
        cameraCenterZ+=newpos.z;
        refreshCamera();
    }
    public void initialize(){
        alpha=Math.PI; //180 degrees
        refreshCamera();
    }
    
    
}
