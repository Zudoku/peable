/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import java.util.ArrayList;

/**
 *
 * @author arska
 */
public class Window {

    
    float x;
    float y;
    private final Node guiNode;
    public Window(float x,float y,Node guiNode){
        this.x=x;
        this.y=y;
        this.guiNode=guiNode;
    }
   
    public void moveWindow(float movex,float movey){
        float xmovement=x-movex;
        float ymovement=y-movey;
        x=movex;
        y=movey;
        
    }
}
