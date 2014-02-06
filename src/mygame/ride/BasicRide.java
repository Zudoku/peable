/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.Main;
import mygame.npc.ActionType;
import mygame.npc.Guest;
import mygame.npc.NPCAction;
import mygame.npc.inventory.RideType;
import mygame.shops.Employee;
import mygame.shops.ShopReputation;
import mygame.terrain.Direction;
import mygame.terrain.MapPosition;
import mygame.terrain.PayParkEvent;
import mygame.terrain.RideDemolishEvent;
import mygame.terrain.RoadMaker;

/**
 *
 * @author arska
 */

public class BasicRide {
    //DEPENDENCIES
    @Inject RoadMaker roadMaker;
    @Inject protected EventBus eventBus;
    //OWNS
    private CustomAnimation animatedPart; //animated part
    private ArrayList<Spatial> staticParts=new ArrayList<Spatial>(); // non-animated parts
    public Enterance enterance;
    public Enterance exit;
    private ArrayList<Employee> employees = new ArrayList<Employee>();
    private ArrayList<Guest> guestsInRide = new ArrayList<Guest>();
    private ArrayList<Guest> guestsInQue = new ArrayList<Guest>();
    //VARIABLES
    private int rideID = 0; //every ride has its own id in parks
    private float price = 1; //charged from the customers every time they visit your ride
    private Direction facing; //not implemented yet
    private MapPosition position;
    public float constructionmoney = 0; //how much did it cost to build this building
    private String rideName = "You found a bug"; //rides name
    private ShopReputation reputation = ShopReputation.NEW; //not implemented yet
    private int rideLength = 10000; //ms = 10s ! How long the ride last
    public RideType rideType; //what type this ride is
    private int exitement =80; //what exitement rate this ride has
    private int nausea = 10; //what nausea rate this ride has
    private boolean status=false; //is the ride open or not true=open
    private int broken=0; //how broken it is  100=broken 0=full repaired
    private int customerstotal=0; //how many customers total
    private float moneytotal=0; //how much money gained total
    private long lastGuestVisitTime=0; //how much time since last guest visited this ride
    private double guestRateHour=0; //how many guests per hour
    private int repairCost=100; //how much does it cost to repair this
    private String ride="you found a bug"; //this is variable used in saving
    
    //TODO:!! 
    private boolean[][] occupySpace = {
        {false, false, false, false},
        {false, false, false, false},
        {false, false, false, false},
        {false, false, false, false},
    };
    private final Node rootNode;
    
    public void setRideType(String ride){
        this.ride=ride;
    }
    
    private void calculateguestRate(){
        //TODO updateguestRate method that does this and modify this to just calculate the rate
        double a=System.currentTimeMillis()-lastGuestVisitTime; //aika jolloin laitteeseen tuli tybä
        double u=3600000/a; //tunti / a
        guestRateHour=u;
        lastGuestVisitTime=System.currentTimeMillis();
    }
    
    public BasicRide(MapPosition position,CustomAnimation object,ArrayList<Spatial> staticParts, float cost, Direction facing,String ride,Node rootNode) {
        this.position = position;
        this.animatedPart = object;
        this.constructionmoney = cost;
        this.facing = facing;
        this.ride=ride;
        this.rootNode=rootNode;
        this.staticParts=staticParts;
        Main.injector.injectMembers(this);
        //aseta oikeelle paikalle
        object.getObject().setLocalTranslation(position.getVector()); //temp fix
        for(Spatial s:staticParts){
            s.setLocalTranslation(position.getVector());
            System.out.println("Object "+s+" moved to "+s.getLocalTranslation());
        }
        
    }

    public void interact(Guest guest) {
        
    }
    

    public boolean tryToQueGuest(Guest guest) {
        if(status){
            guestsInQue.add(guest);
            return true;
        }
        

        return false;
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
            ArrayList<Spatial> linkedroads = roadMaker.getlinkedqueroads(enterance.connectedRoad);
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
        g.getGeometry().setLocalTranslation(position.getVector());
        g.wallet.pay(price);
        eventBus.post(new PayParkEvent(price));
        eventBus.post(new UpdateMoneyTextBarEvent());
        moneytotal += price;
        g.deleteActions();
        calculateguestRate();
        customerstotal++;
    }

    private void leaveRide(Guest g) {
        //delete guest from the list
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
    public Spatial getGeometry(){
        return animatedPart.getObject();
    }
    public int customers(){
        return guestsInRide.size();
    }
    public Vector3f getPositionVector(){
        return position.getVector();
    }
    public MapPosition getPosition(){
        return position;
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

    public void demolish() {

        eventBus.post(new PayParkEvent(0.5f*constructionmoney));
        eventBus.post(new RideDemolishEvent(this));
        eventBus.post(new UpdateMoneyTextBarEvent());

    }
    public void update(){
        
    }
    public void runAnim(){
        animatedPart.runAnimation();
    }
    public void attachToNode(Node node){
        node.attachChild(animatedPart.getObject());
        for(Spatial s:staticParts){
            node.attachChild(s);
        }
    }/**
     * Should be called when deleting ride from map
     * @param node 
     */
    public void detachFromNode(Node node){
        node.detachChild(animatedPart.getObject());
        node.detachChild(enterance.object);
        node.detachChild(exit.object);
        for(Spatial s: staticParts){
            node.detachChild(s);
        }
    }
    public ArrayList<Spatial> getAllSpatialsFromRide(){
        ArrayList<Spatial> list=new ArrayList<Spatial>(staticParts);
        if(exit!=null){
            list.add(exit.object);
        }
        if(enterance!=null){
            list.add(enterance.object);
        }
        if(animatedPart!=null){
            list.add(animatedPart.getObject());
        }
        return list;
    }
    public void setAllSpatialsUserData(String key,Object data){
        for(Spatial s:getAllSpatialsFromRide()){
            s.setUserData(key, data);
        }
    }

    
}
