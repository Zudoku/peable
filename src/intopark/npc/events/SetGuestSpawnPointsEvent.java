/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.events;

import intopark.util.MapPosition;
import java.util.List;

/**
 *
 * @author arska
 */
public class SetGuestSpawnPointsEvent {
    private List<MapPosition>spawnpoints;
    /**
     * 
     * @param spawnpoints 
     */
    public SetGuestSpawnPointsEvent(List<MapPosition> spawnpoints) {
        this.spawnpoints = spawnpoints;
    }

    public List<MapPosition> getSpawnpoints() {
        return spawnpoints;
    }

    public void setSpawnpoints(List<MapPosition> spawnpoints) {
        this.spawnpoints = spawnpoints;
    }

    
}
