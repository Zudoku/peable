/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;
import intopark.inout.LoadPaths;
import intopark.UtilityMethods;
import intopark.roads.BuildingEnterance;
import intopark.roads.events.CreateBuildingEnteranceEvent;
import java.io.IOException;
import intopark.util.Direction;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class Enterance implements Savable{
  private boolean exit=false;
  private MapPosition location;
  private Direction direction;
  private int ID;
  private boolean connected=false;
  private transient Spatial object;
  private transient BasicRide connectedRide;
  private transient BuildingEnterance enteranceWalkable;
  private transient EventBus eventBus;


    public Enterance(boolean exit,MapPosition location,Direction direction,int ID,EventBus eventBus) {
        this.exit=exit;
        this.location=location;
        this.direction=direction;
        this.eventBus=eventBus;
        if(exit==true){
            object=UtilityMethods.loadModel(LoadPaths.rideExit);
        }
        else{
            object=UtilityMethods.loadModel(LoadPaths.rideEnterance);
        }
        object.setLocalTranslation(location.getVector());

        MapPosition l=new MapPosition(location);
        l.setX(l.getX()+direction.directiontoPosition().getX());
        l.setZ(l.getZ()+direction.directiontoPosition().getZ());

        enteranceWalkable=new BuildingEnterance(l,0,BuildingEnterance.RIDE);
        object.setUserData("type","enterance");
        object.setUserData("ID",ID);
        float angle;
        switch(direction){
            case NORTH:

                break;
            case SOUTH:
                angle = (float) Math.toRadians(180);
                this.object.rotate(0, angle, 0);
                break;
            case WEST:
                angle = (float) Math.toRadians(90);
                this.object.rotate(0, angle, 0);
                break;
            case EAST:
                angle = (float) Math.toRadians(-90);
                this.object.rotate(0, angle, 0);
        }
        eventBus.post(new CreateBuildingEnteranceEvent(enteranceWalkable));
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
        this.enteranceWalkable.setID(connectedRide.getID());
    }


    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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


    public Direction getDirection() {
        return direction;
    }

    public MapPosition getLocation() {
        return location;
    }

    public Spatial getObject() {
        return object;
    }

    public int getID() {
        return ID;
    }

}
