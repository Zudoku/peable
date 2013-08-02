/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import mygame.terrain.WorldHandler;

/**
 *
 * @author arska
 */
public class ClickingHandler {

    private final WorldHandler worldHandler;
    public ClickingModes clickMode = ClickingModes.TERRAIN;

    public ClickingHandler(WorldHandler worldHandler) {
        this.worldHandler = worldHandler;
        
    }

    public void handleClicking(CollisionResult target, CollisionResults results) {
        switch (clickMode) {
            case TERRAIN:
                    if(worldHandler.mode==1){
                        
                        worldHandler.lowerland(target);
                    }
                    if(worldHandler.mode==2){
                        
                        worldHandler.raiseland(target);
                    }
                break;

            case NOTHING:
                    
                break;

            case ROAD:

                break;

            case DECORATION:

                break;

            case RIDE:
                break;
        }


    }

    
}
