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
public class RoadTransformContainer {
    public MapPosition pos;
    public Direction facing;
    public RoadHill roadHill;

    public RoadTransformContainer(MapPosition pos, Direction facing, RoadHill roadHill) {
        this.pos = pos;
        this.facing = facing;
        this.roadHill = roadHill;
    }
    public boolean match(RoadTransformContainer rtc2){
        boolean match=true;
        if(!pos.isSameMainCoords(rtc2.pos)){
            match=false;
        }
        if(facing!=rtc2.facing){
            if(!(facing== Direction.ANY||rtc2.facing==Direction.ANY)){
                match=false;
            }
        }
        if(roadHill!=rtc2.roadHill){
            match=false;
        }
        return match;
    }
    
}
