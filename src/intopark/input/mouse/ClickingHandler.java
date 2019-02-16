/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.input.mouse;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.collision.CollisionResults;
import java.util.logging.Logger;
import intopark.Gamestate;
import intopark.gameplayorgans.ClickModeManager;
import intopark.input.SetClickModeEvent;
import static intopark.input.mouse.ClickingModes.TERRAIN;
import intopark.ride.RideManager;
import intopark.shops.ShopManager;
import intopark.terrain.ParkHandler;
import intopark.roads.RoadManager;
import intopark.terrain.TerrainHandler;
import intopark.terrain.decoration.DecorationManager;
import java.util.logging.Level;

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
    @Inject private RoadManager roadMaker;
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
    public void handleCursorHover(float posX,float posY,CollisionResults results){
        getCurrentCaller();
        mouseController.handleOnCursorHover(posX, posY, results);
    }

    private NeedMouse getCurrentCaller(){
        switch(clickMode){
            case DECORATION:
                mouseController.setCallable(decorationManager);
                return decorationManager;
            case DEMOLITION:
                mouseController.setCallable(defaultController);
                return decorationManager;

            case NOTHING:
                mouseController.setCallable(defaultController);
                return defaultController;

            case PLACE:
                mouseController.setCallable(shopManager);
                return shopManager;

            case RIDE:
                mouseController.setCallable(rideManager);
                return rideManager;

            case ROAD:
                mouseController.setCallable(roadMaker);
                return roadMaker;

            case TERRAIN:
                mouseController.setCallable(terrainHandler);
                return terrainHandler;

            default:
                return null;
        }
    }
    public ClickingModes getClickMode() {
        return clickMode;
    }

    public  void setClickMode(ClickingModes clickMode) {
        NeedMouse currentCaller = getCurrentCaller();
        if(currentCaller instanceof ClickModeManager){
            ((ClickModeManager)currentCaller).cleanUp();
        }
        this.clickMode = clickMode;

        currentCaller = getCurrentCaller();
        if(currentCaller instanceof ClickModeManager){
            ((ClickModeManager)currentCaller).onSelection();
        }
    }

    @Subscribe
    public void listenSetClickModeEvent(SetClickModeEvent event){
        setClickMode(event.clickmode);
    }
}
