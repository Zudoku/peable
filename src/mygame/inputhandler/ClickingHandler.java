/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.inputhandler;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.GUI.WindowMaker;
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.Guest;
import mygame.ride.BasicRide;
import mygame.shops.BasicShop;
import mygame.terrain.DeleteSpatialFromMapEvent;
import mygame.terrain.MapContainer;
import mygame.terrain.ParkHandler;
import mygame.terrain.PayParkEvent;
import mygame.terrain.RoadMaker;
import mygame.terrain.RoadMakerStatus;
import mygame.terrain.TerrainHandler;
import mygame.terrain.decoration.DecorationManager;

/**
 *
 * @author arska
 */
@Singleton
public class ClickingHandler {

    @Inject private TerrainHandler worldHandler;
    @Inject EventBus eventBus;
    @Inject WindowMaker windowMaker;
    public ClickingModes clickMode = ClickingModes.NOTHING;
    public int buffer = 0;
    private final Node rootNode;
    private final ParkHandler parkHandler;
    private final RoadMaker roadMaker;
    private final MapContainer map;
    private final DecorationManager decorationManager;

    @Inject
    public ClickingHandler(Node rootNode,ParkHandler parkHandler,RoadMaker roadMaker,MapContainer map,DecorationManager decorationManager) {
        
        this.rootNode=rootNode;
        this.parkHandler=parkHandler;
        this.roadMaker=roadMaker;
        this.map=map;
        this.decorationManager=decorationManager;
        
    }

    public void handleClicking(CollisionResult target, CollisionResults results) {
        switch (clickMode) {
            case TERRAIN:

                if (worldHandler.mode == 1) {

                    worldHandler.lowerland(target);
                }
                if (worldHandler.mode == 2) {

                    worldHandler.raiseland(target);
                }
                Gamestate.ingameHUD.updateClickingIndicator();
                break;

            case NOTHING:
                System.out.println(target.getGeometry().getName() + "   " + target.getGeometry().getLocalTranslation() + "   " + target.getContactPoint());


                if (target.getGeometry().getParent() == null) {
                    return;
                }
                if (target.getGeometry().getParent().getParent() == null) {
                    return;
                }

                Node rootTarget = target.getGeometry().getParent().getParent();

                if (rootTarget.getUserData("guestnum") != null) {
                    for (Guest g : Main.gamestate.npcManager.guests) {
                        if (g.getGuestNum() == rootTarget.getUserData("guestnum")) {
                            windowMaker.createGuestWindow(g, true);
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("shopID") != null) {
                    for (BasicShop g : Main.gamestate.shopManager.shops) {
                        if (g.shopID == rootTarget.getUserData("shopID")) {
                            windowMaker.createShopWindow(g);
                            return;
                        }
                    }
                }
                if (rootTarget.getUserData("type") == "ride") {
                    for (BasicRide r : Main.gamestate.rideManager.rides) {
                        if (r.getRideID() == rootTarget.getUserData("rideID")) {
                            windowMaker.CreateRideWindow(r);
                        }
                    }
                }

                break;

            case ROAD:
                if (Main.gamestate.roadMaker.status == RoadMakerStatus.CHOOSING) {
                    Main.gamestate.roadMaker.startingPosition(target.getContactPoint());
                    Gamestate.ingameHUD.updateClickingIndicator();
                }
                break;

            case DECORATION:
                decorationManager.build(target.getContactPoint());
                Gamestate.ingameHUD.updateClickingIndicator();
                break;

            case RIDE:
                Main.gamestate.rideManager.placeEnterance(target.getContactPoint());
                Gamestate.ingameHUD.updateClickingIndicator();
                break;

            case PLACE:
                if (buffer == 0) {
                    Main.gamestate.shopManager.buy(parkHandler);
                    Main.gamestate.holoDrawer.toggleDrawSpatial();
                    Gamestate.ingameHUD.updateClickingIndicator();
                } else {
                    buffer = buffer - 1;
                }
                break;

        }


    }

    void handleRightClicking(CollisionResult target, CollisionResults results) {
        System.out.println(target.getGeometry().getName() + "   " + target.getGeometry().getLocalTranslation() + "   " + target.getContactPoint());


        if (target.getGeometry().getParent() == null) {
            return;
        }
        if (target.getGeometry().getParent().getParent() == null) {
            return;
        }

        Node rootTarget = target.getGeometry().getParent().getParent();


        switch (clickMode) {

            case NOTHING:
                
                break;
                
            case DECORATION:

                break;

            case DEMOLITION:
                if(rootTarget.getUserData("type")==null){
                    return;
                }
                
                if (rootTarget.getUserData("type").equals("road")) {
                    rootNode.detachChild(rootTarget);
                    deleteFromMap(rootTarget);
                    eventBus.post(new PayParkEvent(5f)); //tien hinnasta osa takasin
                    eventBus.post(new UpdateMoneyTextBarEvent());
                }
                if (rootTarget.getUserData("type").equals("decoration")) {
                    Node decorationnode=(Node)rootNode.getChild("decorationNode");
                    decorationnode.detachChild(rootTarget);
                    eventBus.post(new DeleteSpatialFromMapEvent(rootTarget));
                    
                }

        }
    }

    private void deleteFromMap(Node rootTarget) {
        
        for(int y=0;y<25;y++){
            for(int x=0;x<parkHandler.getMapHeight();x++){
                for(int z=0;z<parkHandler.getMapWidth();z++){
                    if(map.getMap()[x][y][z]!=null){
                        if(map.getMap()[x][y][z]==rootTarget){
                            map.getMap()[x][y][z]=null;
                            if(rootTarget.getUserData("type")!=null){
                                if (rootTarget.getUserData("type").equals("road")) {
                                    roadMaker.updateroads(x, y, z);
                                    roadMaker.updateroads(x, y+1, z);
                                    roadMaker.updateroads(x, y-1, z);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
