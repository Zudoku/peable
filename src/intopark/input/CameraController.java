/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input;

import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.util.logging.Level;
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
    private EventBus eventBus;
    //VARIABLES
    private static float MOVESPEED=0.15f; // Speed that the camera moves
    private static double ROTATESPEED=0.02; //Speed that te camera rotates
    private float cameraHeight=30; // Height where the camera is
    private float radius=20; //Circe radius which the camera rotates around
    private double alpha=0; // Alpha angle in radians
    private float cameraCenterX=0; //Camera offset X - Also the coordinate which the camera looks at.
    private float cameraCenterZ=5; //Camera offset Z - Also the coordinate which the camera looks at.
    private float cameraCenterY=6; //Camera offset Y - Also the coordinate which the camera looks at.

    public CameraController(Camera camera,EventBus eventBus){
        this.camera=camera;
        this.eventBus =eventBus;
        refreshCamera();
    }
    public void onTurnCamera(boolean right){

        turnCamera(right);
        refreshCamera();
        eventBus.post(new CameraTurnedEvent(getCameraCompass()));
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
    /**
     * MAGIC! :D
     * @return cameras compass angle in degrees
     */
    public double getCameraCompass(){
        Vector3f cameraDir=camera.getDirection();
        double translated_x = cameraDir.x / cameraDir.y;
        double translated_y = cameraDir.z / cameraDir.y;

        double side_a = 1d;
        double side_b = Math.sqrt(translated_x*translated_x + translated_y*translated_y); //Pythagoras theory
        double side_c = Math.sqrt((Math.abs(translated_y)+1d)*(Math.abs(translated_y)+1d) + translated_x * translated_x);

        double angle = Math.acos((side_a*side_a + side_b*side_b - side_c*side_c) / (2 * side_a * side_b ));
                        //cos-1 ((side_a^2 + side_b^2-side_c^2)/2ab)

        angle = Math.toDegrees(angle);

        if(translated_x > 0){
            angle =180+(180-angle);
        }
        angle -=90;
        if(translated_y > 0){
            angle =180 + (180-angle);
        }
        return angle;
    }


}
