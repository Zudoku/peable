/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.google.inject.Inject;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.ActionType;
import mygame.npc.Guest;
import mygame.npc.NPCAction;
import mygame.npc.inventory.PreferredRides;
import mygame.shops.Employee;
import mygame.shops.ShopReputation;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */

public class BasicRide {

    private Direction facing;
    private Vector3f position;
    private Spatial object;
    private int rideID = 0;
    private float price = 1;
    public float constructionmoney = 0;
    private String rideName = "You found a bug";
    public Enterance enterance;
    public Enterance exit;
    private ArrayList<Employee> employees = new ArrayList<Employee>();
    private ShopReputation reputation = ShopReputation.NEW;
    private ArrayList<Guest> guestsInRide = new ArrayList<Guest>();
    private ArrayList<Guest> guestsInQue = new ArrayList<Guest>();
    private int rideLength = 10000; //ms = 10s
    public PreferredRides rideType;
    private int exitement =80;
    private int nausea = 10;
    private boolean status=false; //true=open
    private int broken=0;
    private int customerstotal=0;
    private float moneytotal=0;
    private long lastGuestVisitTime=0;
    private double guestRateHour=0;
    private int repairCost=100;
    private String ride="you found a bug";
    
    //TODO!! 
    private boolean[][] occupySpace = {
        {false, false, false, false},
        {false, false, false, false},
        {false, false, false, false},
        {false, false, false, false},
    };
    
    public void setRideType(String ride){
        this.ride=ride;
    }
    
    private void calculateguestRate(){
        double a=System.currentTimeMillis()-lastGuestVisitTime; //aika jolloin laitteeseen tuli tybä
        double u=3600000/a; //tunti / a
        guestRateHour=u;
        lastGuestVisitTime=System.currentTimeMillis();
    }
    
    public BasicRide(Vector3f position, Spatial object, float cost, Direction facing,String ride) {
        this.position = position;
        this.object = object;
        this.constructionmoney = cost;
        this.facing = facing;
        this.ride=ride;
        
        object.setLocalTranslation(position);
    }

    public void interact(Guest guest) {
        
    }
    

    public boolean tryToQueGuest(Guest guest) {
        guestsInQue.add(guest);

        return true;
    }

    public void updateQueLine() {
        if (guestsInQue.isEmpty()) {
            return;
        }

        for (int i = 0; i < guestsInQue.size(); i++) {
            Guest handledguest = guestsInQue.get(i);
            Spatial testedroad = handledguest.currentQueRoad;
            if (testedroad == null) {
                System.out.println("Road from guest null :(");
            }
            ArrayList<Spatial> linkedroads = Main.gamestate.roadMaker.getlinkedqueroads(enterance.connectedRoad);
            if (!linkedroads.contains(testedroad)) {
                System.out.println("Error");
                continue;
            }
            int a = linkedroads.indexOf(testedroad);
            if (i == a) {
                //Guest is where he is supposed to be

                if (i == 0) {
                    if (handledguest.isEmptyActions()) {
                        takeQuestToRide(handledguest);
                    }
                }
                continue;
            }
            if (i < a) {
                //anna käsky eteenpoäin
                Vector3f newpos = linkedroads.get(i).getLocalTranslation();
                handledguest.callToQueRoad(new NPCAction(newpos, ActionType.QUE, handledguest));
                handledguest.currentQueRoad = linkedroads.get(i);
            }
        }
    }

    public void updateRide() {
        ArrayList<Guest> guestscopy = (ArrayList<Guest>) guestsInRide.clone();
        for (Guest g : guestscopy) {
            if (g.joinedRide + rideLength < System.currentTimeMillis()) {
                leaveRide(g);
            }
        }
    }

    public void takeQuestToRide(Guest g) {
        
        guestsInQue.remove(g);
        guestsInRide.add(g);
        g.joinedRide = System.currentTimeMillis();
        g.getGeometry().setLocalTranslation(position);
        g.wallet.pay(price);
        Main.currentPark.getParkWallet().add(price);
        Gamestate.ingameHUD.updateMoneytextbar();
        moneytotal += price;
        g.deleteActions();
        calculateguestRate();
        customerstotal++;
    }

    private void leaveRide(Guest g) {
        //poista guesti listoilta
        interact(g);
        guestsInRide.remove(g);
        //laita guesti enterancen positioniin
        int x1 = (int) (exit.location.x + 0.4999);
        int y1 = (int) (exit.location.y + 0.4999);
        int z1 = (int) (exit.location.z + 0.4999);
        g.getGeometry().setLocalTranslation(x1, y1, z1);
        g.initXYZ(x1, y1, z1);
        g.active = true;
        

    }
    public int customers(){
        return guestsInRide.size();
    }
    public Vector3f getPosition(){
        return position;
    }
    public Spatial getGeometry() {
        return object;
    }
    public void setRideID(int rideID){
        this.rideID=rideID;
    }
    public int getRideID(){
        return rideID;
    }

    public boolean[][] getOccupySpace() {
        return occupySpace;
    }
    public float getPrice(){
        return price;
    }
    public String getName(){
        return rideName;
    }
    public int getExitement(){
        return exitement;
    }
    public int getNausea(){
        return nausea;
    }
    public boolean getStatus(){
        return status;
    }
    public int getBroken(){
        return broken;
    }
    public int getCustomersTotal(){
        return customerstotal;
    }
    public float getMoneyGainedTotal(){
        return moneytotal;
    }
    public double getGuestRateHour(){
        return guestRateHour;
    }
    public double getMoneyRateHour(){
        return guestRateHour*price;
    }
    public float getRepairCost(){
        return repairCost;
    }

    public void setName(String text) {
        this.rideName=text;
    }

    public String getRide() {
        return ride;
    }

    public void setPrice(float value) {
        this.price=value;
    }
    public boolean toggleStatus(){
        status=!status;
        return status;
    }
    public void setStats(int broken,int exitement,int nausea,boolean status){
        this.broken=broken;
        this.exitement=exitement;
        this.nausea=nausea;
        this.status=status;
    }
    

    
}
