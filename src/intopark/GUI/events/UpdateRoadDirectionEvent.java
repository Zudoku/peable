/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI.events;

import com.google.inject.Singleton;
import intopark.util.Direction;

/**
 *
 * @author arska
 */
@Singleton
public class UpdateRoadDirectionEvent {
    private Direction direction;
    public UpdateRoadDirectionEvent(Direction d){
        this.direction=d;

    }

    public Direction getDirection() {
        return direction;
    }

}
