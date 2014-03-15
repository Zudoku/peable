/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI.events;

import com.google.inject.Singleton;
import intopark.shops.BasicBuildables;

/**
 *
 * @author arska
 */
@Singleton
public class UpdateBuildingUIEvent {
    public BasicBuildables buildable;
    public UpdateBuildingUIEvent(BasicBuildables buildable) {
        this.buildable=buildable;
    }
    
}
