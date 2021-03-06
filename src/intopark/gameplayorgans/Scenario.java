/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.gameplayorgans;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import intopark.Main;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
@Singleton
public class Scenario {
    //DEPENDENCIES
    @Inject private transient EventBus eventBus;
    //VARIABLES
    private MapPosition enterancePos;
    private double enteranceYRotation;
    private int rideID;
    private int shopID;
    private int mapHeight;
    private int mapWidth;
    private int maxGuests;
    private String parkName = "defaultparkname";
    //GOAL POSSIBILITIES
    private int neededGuest;


    public void setUp(){
        Main.injector.injectMembers(this);
    }

    /*
     * SETTERS AND GETTERS
     */


    public void setEnterancePos(MapPosition enterancePos) {
        this.enterancePos = enterancePos;
    }

    public MapPosition getEnterancePos() {
        return enterancePos;
    }

    public void setEnteranceYRotation(double enteranceYRotation) {
        this.enteranceYRotation = enteranceYRotation;
    }

    public double getEnteranceYRotation() {
        return enteranceYRotation;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public int getRideID() {
        return rideID;
    }

    public String getParkName() {
        return parkName;
    }

    public int getShopID() {
        return shopID;
    }

    public void setNeededGuest(int neededGuest) {
        this.neededGuest = neededGuest;
    }

    public int getNeededGuest() {
        return neededGuest;
    }
    public void setMapSize(int height,int width){
        this.mapHeight=height;
        this.mapWidth=width;
    }


}
