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
public class DeleteRoadEvent {
    private Road road;
    private boolean deleteNow;

    public DeleteRoadEvent(Road road,boolean deleteNow) {
        this.road = road;
        this.deleteNow = deleteNow;
    }

    public Road getRoad() {
        return road;
    }

    public boolean isDeleteNow() {
        return deleteNow;
    }

}
