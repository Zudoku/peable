/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.util.Direction;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class BuildingEnterance extends Walkable{
    private int ID;
    private int buildingType;
    private Direction direction;
    public transient static int SHOP=100;
    public transient static int RIDE=150;

    public BuildingEnterance(MapPosition position,int ID,int buildingType) {
        super(position);
        this.ID=ID;
        this.buildingType=buildingType;
    }
    public int getBuildingType() {
        return buildingType;
    }
    public Direction getDirection() {
        return direction;
    }
    public int getID() {
        return ID;
    }
    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

}
