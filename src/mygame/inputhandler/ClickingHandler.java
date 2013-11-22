/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import mygame.Main;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.TerrainHandler;

/**
 *
 * @author arska
 */
public class ClickingHandler {

    public final TerrainHandler worldHandler;
    public ClickingModes clickMode = ClickingModes.NOTHING;
    public int buffer=0;

    public ClickingHandler(TerrainHandler worldHandler) {
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
                System.out.println(target.getGeometry().getName());
                
                
                if(target.getGeometry().getParent()==null){
                    return;
                }
                if(target.getGeometry().getParent().getParent()==null){
                    return;
                }
                
                Node rootTarget=target.getGeometry().getParent().getParent();
                
                if(rootTarget.getUserData("guestnum")!=null){
                    for(Guest g:Main.npcManager.guests){
                        if(g.getGuestNum()==rootTarget.getUserData("guestnum")){
                            Main.windowMaker.createGuestWindow(g,true);
                            return;
                        }
                    }
                }
                if(rootTarget.getUserData("shopID")!=null){
                    for(BasicShop g:Main.shopManager.shops){
                        if(g.shopID==rootTarget.getUserData("shopID")){
                            Main.windowMaker.createShopWindow(g);
                            return;
                        }
                    }
                }
                if(rootTarget.getUserData("type")=="ride"){
                    for(BasicRide r:Main.rideManager.rides){
                        if(r.rideID==rootTarget.getUserData("rideID")){
                            Main.windowMaker.CreateRideWindow(r);
                        }
                    }
                }
                
                break;

            case ROAD:
                    if(Main.roadMaker.status== RoadMakerStatus.CHOOSING){
                        Main.roadMaker.startingPosition(target.getContactPoint());
                    }
                break;

            case DECORATION:

                break;

            case RIDE:
                Main.rideManager.placeEnterance(target.getContactPoint());
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
