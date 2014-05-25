/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inputhandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResults;
import java.util.logging.Logger;
import intopark.Gamestate;
import static intopark.inputhandler.ClickingModes.TERRAIN;
import intopark.ride.RideManager;
import intopark.shops.ShopManager;
import intopark.terrain.ParkHandler;
import intopark.roads.RoadMaker;
import intopark.terrain.TerrainHandler;
import intopark.terrain.decoration.DecorationManager;

/**
 *
 * @author arska
 */
@Singleton
public class ClickingHandler {
    //LOGGER
    private static final Logger logger = Logger.getLogger(ClickingHandler.class.getName());
    //DEPENDENCIES
    private final ParkHandler parkHandler;
    private EventBus eventBus;
    private MouseController mouseController=new MouseController();
    //CALLABLES
    @Inject private TerrainHandler terrainHandler;
    @Inject private RideManager rideManager;
    @Inject private RoadMaker roadMaker;
    @Inject private ShopManager shopManager;
    @Inject private DecorationManager decorationManager;
    @Inject private DefaultMouseController defaultController;
    //VARIABLES
    private ClickingModes clickMode = ClickingModes.NOTHING;
    
    @Inject
    public ClickingHandler(EventBus eventBus,ParkHandler parkHandler) {
        this.parkHandler = parkHandler;
        this.eventBus=eventBus;
        eventBus.register(this);
    }

    public void handleMouseClick(boolean leftClick,float posX,float posY,CollisionResults results){
        getCurrentCaller();
        mouseController.handleClickingMechanic(leftClick, posX, posY, results);
    }
    public void handleMouseDrag(boolean leftClick,float posX,float posY,CollisionResults results,long lastDragged){
        getCurrentCaller();
        mouseController.handleDragMechanic(leftClick,posX,posY,results,lastDragged);
    }
    public void handleMouseDragRelease(float posX,float posY,CollisionResults results){
        getCurrentCaller();
        mouseController.handleReleaseDragMechanic(posX, posY, results);
    }
    private void getCurrentCaller(){
        switch(clickMode){
            case DECORATION:
                mouseController.setCallable(decorationManager);
                return;
            case DEMOLITION:
                mouseController.setCallable(defaultController);
                return;
                
            case NOTHING:
                mouseController.setCallable(defaultController);
                return;
                
            case PLACE:
                mouseController.setCallable(shopManager);
                return;
                
            case RIDE:
                mouseController.setCallable(rideManager);
                return;
                
            case ROAD:
                mouseController.setCallable(roadMaker);
                return;
                
            case TERRAIN:
                mouseController.setCallable(terrainHandler);
        }
    }
    public ClickingModes getClickMode() {
        return clickMode;
    }

    public  void setClickMode(ClickingModes clickMode) {
        this.clickMode = clickMode;
        Gamestate.ingameHUD.updateClickingIndicator();
    }
    
    @Subscribe
    public void listenSetClickModeEvent(SetClickModeEvent event){
        clickMode=event.clickmode;
    }
}
