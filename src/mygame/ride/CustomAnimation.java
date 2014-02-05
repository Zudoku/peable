/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.jme3.scene.Spatial;
import java.util.Random;

/**
 *
 * @author arska
 */
public class CustomAnimation {
    private static final int IDLE=0;
    private static final int RUN=1;
    private static final int BROKEN=2;
    
    private AnimationType type=AnimationType.STATIC;
    private int status=RUN;
    
    private Spatial object;
    
    private double length=1000;
    private int speed=10;
    public CustomAnimation(Spatial object){
        this.object=object;
    }

    public Spatial getObject() {
        return object;
    }
    //call every frame to run the animation 
    public void runAnimation(){
        switch(status){
            case RUN:
                //run anim
                switch(type){
                    case STATIC:
                    Random r=new Random();
                    rollV(r.nextFloat());
                    break;
                }
                break;
                
            case IDLE:
                //reset
                break;
                
            case BROKEN:
                //do nothing
                break;
        }
        
    }

    public void setObject(Spatial object) {
        this.object = object;
    }
    private void rollV(float degrees){
        float angle = (float) Math.toRadians(degrees);
        object.rotate(0, angle, 0);
    }
    private void rollH(float degrees){
        float angle = (float) Math.toRadians(degrees);
        object.rotate(angle,0,0);
    }
    
}
