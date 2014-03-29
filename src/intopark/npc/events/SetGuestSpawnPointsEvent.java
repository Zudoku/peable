/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.events;

import com.jme3.math.Vector3f;
import java.util.List;

/**
 *
 * @author arska
 */
public class SetGuestSpawnPointsEvent {
    private List<Vector3f>spawnpoints;
    /**
     * 
     * @param spawnpoints 
     */
    public SetGuestSpawnPointsEvent(List<Vector3f> spawnpoints) {
        this.spawnpoints = spawnpoints;
    }

    public List<Vector3f> getSpawnpoints() {
        return spawnpoints;
    }

    public void setSpawnpoints(List<Vector3f> spawnpoints) {
        this.spawnpoints = spawnpoints;
    }
    
}
