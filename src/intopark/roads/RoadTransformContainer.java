/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.terrain.Direction;
import intopark.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class RoadTransformContainer {
    public MapPosition pos;
    public Direction facing;
    public RoadHill roadHill;

    public RoadTransformContainer(MapPosition pos, Direction facing, RoadHill roadHill) {
        this.pos = pos;
        this.facing = facing;
        this.roadHill = roadHill;
    }
    
}
