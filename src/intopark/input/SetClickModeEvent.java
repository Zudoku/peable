/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input;

import intopark.input.mouse.ClickingModes;

/**
 *
 * @author arska
 */
public class SetClickModeEvent {
    public ClickingModes clickmode;

    public SetClickModeEvent(ClickingModes clickmode) {
        this.clickmode = clickmode;
    }
    
}
