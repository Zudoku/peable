/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class Enterance {
  public boolean exit=false;
  //tiepala johon kiinni
  Vector3f location;
  Direction facing;
  Spatial object;
  BasicRide connectedRide;

    public Enterance(boolean exit,Vector3f location,Direction facing,AssetManager assetManager) {
        this.exit=exit;
        this.location=location;
        this.facing=facing;
        
        if(exit==true){
            object=assetManager.loadModel("Models/Rides/exit.j3o");
        }
        else{
            object=assetManager.loadModel("Models/Rides/enterace.j3o");
        }
        object.setLocalTranslation(location);
        float angle;
        switch(facing){
            case UP:
                
                break;
            case DOWN:
                angle = (float) Math.toRadians(180);
                this.object.rotate(0, angle, 0);
                break;
            case LEFT:
                angle = (float) Math.toRadians(90);
                this.object.rotate(0, angle, 0);
                break;
            case RIGHT:
                angle = (float) Math.toRadians(-90);
                this.object.rotate(0, angle, 0);
        }
    }
  
  
}
