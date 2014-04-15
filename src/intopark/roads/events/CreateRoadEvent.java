/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads.events;

import intopark.roads.Road;

/**
 *
 * @author arska
 */
public class CreateRoadEvent {
    private Road road;

    public CreateRoadEvent(Road road) {
        this.road = road;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }
    
}
