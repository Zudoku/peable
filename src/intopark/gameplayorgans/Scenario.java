/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.gameplayorgans;

import com.google.inject.Singleton;
import com.jme3.math.Vector3f;

/**
 *
 * @author arska
 */
@Singleton
public class Scenario {
    //DEPENDENCIES
    //VARIABLES
    private ScenarioGoal goal;
    private transient Vector3f enterancePos;
    private double enteranceYRotation;
    private int rideID;
    private int shopID;
    private int mapHeight;
    private int mapWidth;
    private int maxGuests;
    private String parkName = "defaultparkname";
    //GOAL POSSIBILITIES
    private int neededGuest;

    public Scenario(ScenarioGoal goal) {
        this.goal = goal;
    }

    public void setEnterancePos(Vector3f enterancePos) {
        this.enterancePos = enterancePos;
    }

    public Vector3f getEnterancePos() {
        return enterancePos;
    }

    public void setEnteranceYRotation(double enteranceYRotation) {
        this.enteranceYRotation = enteranceYRotation;
    }

    public double getEnteranceYRotation() {
        return enteranceYRotation;
    }

    public void setGoal(ScenarioGoal goal) {
        this.goal = goal;
    }

    public ScenarioGoal getGoal() {
        return goal;
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
