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
    Direction d;
    public UpdateRoadDirectionEvent(Direction d){
        this.d=d;
        
    }

    public Direction getD() {
        return d;
    }
    
}
