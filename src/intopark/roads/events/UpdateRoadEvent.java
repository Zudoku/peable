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
public class UpdateRoadEvent {
    private Road existingRoad;
    
    public UpdateRoadEvent(Road existingRoad) {
        this.existingRoad = existingRoad;
    }

    public Road getExistingRoad() {
        return existingRoad;
    }

    public void setExistingRoad(Road existingRoad) {
        this.existingRoad = existingRoad;
    }
    
}
