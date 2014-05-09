/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads.events;

import intopark.roads.BuildingEnterance;

/**
 *
 * @author arska
 */
public class CreateBuildingEnteranceEvent {
    private BuildingEnterance enterance;
    public CreateBuildingEnteranceEvent(BuildingEnterance enterance) {
        this.enterance=enterance;
    }

    public BuildingEnterance getEnterance() {
        return enterance;
    }

}
