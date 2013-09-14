/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import mygame.Main;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.WorldHandler;

/**
 *
 * @author arska
 */
public class ClickingHandler {

    public final WorldHandler worldHandler;
    public ClickingModes clickMode = ClickingModes.NOTHING;
    public int buffer=0;

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
                    if(Main.roadMaker.status== RoadMakerStatus.CHOOSING){
                        Main.roadMaker.startingPosition(target.getContactPoint());
                    }
                break;

            case DECORATION:

                break;

            case RIDE:
                
                break;
                
            case PLACE:
                if(buffer==0){
                    Main.shopManager.buy();
                    Main.holoDrawer.toggleDrawSpatial();
                }
                else{
                    buffer=buffer-1;
                }
                break;
                
        }


    }

    
}
