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
    public Direction direction;
    public RoadHill roadHill;

    public RoadTransformContainer(MapPosition pos, Direction direction, RoadHill roadHill) {
        this.pos = pos;
        this.direction = direction;
        this.roadHill = roadHill;
    }
    public boolean match(RoadTransformContainer rtc2){
        boolean match=true;
        if(!pos.isSameMainCoords(rtc2.pos)){
            match=false;
        }
        if(direction!=rtc2.direction){
            if(!(direction== Direction.ANY||rtc2.direction==Direction.ANY)){
                match=false;
            }
        }
        if(roadHill!=rtc2.roadHill){
            match=false;
        }
        return match;
    }
    
}
