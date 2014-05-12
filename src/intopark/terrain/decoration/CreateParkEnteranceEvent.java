/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.decoration;

import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class CreateParkEnteranceEvent {
    public double rotate;
    public MapPosition position;

    public CreateParkEnteranceEvent(double rotate, MapPosition position) {
        this.rotate = rotate;
        this.position = position;
    }
    
}
