/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class BuildingSelectionEvent {
    BasicBuildables selection;
    public BuildingSelectionEvent(BasicBuildables selection) {
        this.selection=selection;
    }
    
}
