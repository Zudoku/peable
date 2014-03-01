/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class BuildingSelectionEvent {
    int tab;
    int index;
    public BuildingSelectionEvent(int tab, int index) {
        this.tab=tab;
        this.index=index;
    }
    
}
