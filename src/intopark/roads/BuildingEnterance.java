/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class BuildingEnterance extends Walkable{
    private int ID;
    private int buildingType;
    public BuildingEnterance(MapPosition position,int ID,int buildingType) {
        super(position);
        this.ID=ID;
        this.buildingType=buildingType;
    }
    
}
