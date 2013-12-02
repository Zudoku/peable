/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.inject.Inject;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import mygame.Gamestate;
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
    @Inject
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
                    Gamestate.ingameHUD.updateClickingIndicator();
                break;

            case NOTHING:
                System.out.println(target.getGeometry().getName()+"   "+target.getGeometry().getLocalTranslation()+"   "+target.getContactPoint());
                
                
                if(target.getGeometry().getParent()==null){
                    return;
                }
                if(target.getGeometry().getParent().getParent()==null){
                    return;
                }
                
                Node rootTarget=target.getGeometry().getParent().getParent();
                
                if(rootTarget.getUserData("guestnum")!=null){
                    for(Guest g:Main.gamestate.npcManager.guests){
                        if(g.getGuestNum()==rootTarget.getUserData("guestnum")){
                            Main.gamestate.windowMaker.createGuestWindow(g,true);
                            return;
                        }
                    }
                }
                if(rootTarget.getUserData("shopID")!=null){
                    for(BasicShop g:Main.gamestate.shopManager.shops){
                        if(g.shopID==rootTarget.getUserData("shopID")){
                            Main.gamestate.windowMaker.createShopWindow(g);
                            return;
                        }
                    }
                }
                if(rootTarget.getUserData("type")=="ride"){
                    for(BasicRide r:Main.gamestate.rideManager.rides){
                        if(r.getRideID()==rootTarget.getUserData("rideID")){
                            Main.gamestate.windowMaker.CreateRideWindow(r);
                        }
                    }
                }
                
                break;

            case ROAD:
                    if(Main.gamestate.roadMaker.status== RoadMakerStatus.CHOOSING){
                        Main.gamestate.roadMaker.startingPosition(target.getContactPoint());
                        Gamestate.ingameHUD.updateClickingIndicator();
                    }
                break;

            case DECORATION:
                Gamestate.ingameHUD.updateClickingIndicator();
                break;

            case RIDE:
                Main.gamestate.rideManager.placeEnterance(target.getContactPoint());
                Gamestate.ingameHUD.updateClickingIndicator();
                break;
                
            case PLACE:
                if(buffer==0){
                    Main.gamestate.shopManager.buy();
                    Main.gamestate.holoDrawer.toggleDrawSpatial();
                    Gamestate.ingameHUD.updateClickingIndicator();
                }
                else{
                    buffer=buffer-1;
                }
                break;
                
        }


    }

    
}
