/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class Walkable {
    protected MapPosition position;
    private boolean needsUpdate = false;

    public Walkable(MapPosition position) {
        this.position = position;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }
    
    
}