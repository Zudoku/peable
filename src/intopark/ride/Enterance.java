/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;
import intopark.inout.LoadPaths;
import intopark.UtilityMethods;
import java.io.IOException;
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class Enterance implements Savable{
  private boolean exit=false;
  private MapPosition location;
  private Direction facing;
  private boolean connected=false;
  private transient Spatial object;
  private transient BasicRide connectedRide;
  private transient Spatial connectedRoad;
  

    public Enterance(boolean exit,MapPosition location,Direction facing) {
        this.exit=exit;
        this.location=location;
        this.facing=facing;
        
        if(exit==true){
            object=UtilityMethods.loadModel(LoadPaths.rideExit);
        }
        else{
            object=UtilityMethods.loadModel(LoadPaths.rideEnterance);
        }
        object.setLocalTranslation(location.getVector());
        //object.setUserData("enterance",this);
        object.setUserData("type","enterance");
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

    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * GETTERS AND SETTERS.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setConnectedRide(BasicRide connectedRide) {
        this.connectedRide = connectedRide;
    }

    public void setConnectedRoad(Spatial connectedRoad) {
        this.connectedRoad = connectedRoad;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public void setLocation(MapPosition location) {
        this.location = location;
    }

    public void setObject(Spatial object) {
        this.object = object;
    }

    public BasicRide getConnectedRide() {
        return connectedRide;
    }

    public Spatial getConnectedRoad() {
        return connectedRoad;
    }

    public Direction getFacing() {
        return facing;
    }

    public MapPosition getLocation() {
        return location;
    }

    public Spatial getObject() {
        return object;
    }
}
