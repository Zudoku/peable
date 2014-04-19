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
    private boolean[] connected;
    
    public UpdateRoadEvent(Road existingRoad,boolean[] connected) {
        this.existingRoad = existingRoad;
        this.connected=connected;
    }

    public void setConnected(boolean[] connected) {
        this.connected = connected;
    }

    public boolean[] getConnected() {
        return connected;
    }

    public Road getExistingRoad() {
        return existingRoad;
    }

    public void setExistingRoad(Road existingRoad) {
        this.existingRoad = existingRoad;
    }
    
}
