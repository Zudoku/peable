/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.jme3.math.Transform;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
    private Transform originalTransform;
    private Spatial object;
    
    private int speed=10;
    private boolean reseted=true;
    public CustomAnimation(Spatial object,AnimationType type){
        this.object=object;
        originalTransform=object.getLocalTransform();
        this.type=type;
    }
    //blank animation
    public CustomAnimation(){
        status=BROKEN;
        object=new Node("nullSpatial");
    }

    public Spatial getObject() {
        return object;
    }
    //call every frame to run the animation 
    public void runAnimation(){
        switch(status){
            case RUN:
                if(reseted){
                    reseted=false;
                }
                //run anim
                switch(type){
                    case STATIC:
                        //blank anim
                        break;
                        
                    case ROLLV:
                        rollV(speed);
                        break;
                        
                    case ROLLH:
                        rollH(speed);
                        break;
                }
                break;
                
            case IDLE:
                if(!reseted){
                    reset();
                }
                break;
                
            case BROKEN:
                //do nothing because broken
                break;
        }
        
    }

    public void setObject(Spatial object) {
        this.object = object;
    }
    private void rollV(float degrees){
        float angle = (float) Math.toRadians(degrees/10);
        object.rotate(0, angle, 0);
    }
    private void rollH(float degrees){
        float angle = (float) Math.toRadians(degrees/10);
        object.rotate(angle,0,0);
    }
    public void setStatus(int status){
        this.status=status;
    }
    private void reset(){
        object.setLocalTransform(originalTransform);
        reseted=true;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    
}
