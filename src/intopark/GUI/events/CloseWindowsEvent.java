/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI.events;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class CloseWindowsEvent {
    public String parameters;
    /**
     * "" - Empty String will clear all windows
     * @param parameters Check IngameHUD - CloseWindows(String) for details
     */
    public CloseWindowsEvent(String parameters) {
        this.parameters=parameters;
    }
    
}
