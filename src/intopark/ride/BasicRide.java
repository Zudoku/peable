/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import intopark.GUI.events.UpdateMoneyTextBarEvent;
import intopark.Main;
import intopark.npc.Guest;
import intopark.npc.inventory.RideType;
import intopark.shops.ShopReputation;
import intopark.util.Direction;
import intopark.util.MapPosition;
import intopark.terrain.events.PayParkEvent;
import intopark.terrain.events.RideDemolishEvent;
import intopark.roads.RoadMaker;
import java.util.List;

/**
 *
 * @author arska
 */
public class BasicRide {
    //LOGGER
    protected transient static final Logger logger = Logger.getLogger(BasicRide.class.getName());
    //DEPENDENCIES
    @Inject transient RoadMaker roadMaker;
    @Inject transient protected EventBus eventBus;
    //OWNS
    private transient CustomAnimation animatedPart; //animated part
    private transient List<Spatial> staticParts=new ArrayList<>(); // non-animated parts
    private Enterance enterance;
    private Enterance exit;
    private transient List<Guest> guestsInRide = new ArrayList<>();
    private transient List<Guest> guestsInQue = new ArrayList<>();
    //VARIABLES
    private int rideID = 0; //every ride has its own id in parks
    private float price = 1; //charged from the customers every time they visit your ride
    private Direction direction; //not implemented yet
    private MapPosition position; //position of this ride
    private float constructionmoney = 0; //how much did it cost to build this building
    private String rideName = "You found a bug"; //rides name
    private ShopReputation reputation = ShopReputation.NEW; //not implemented yet
    private int rideLength = 10000; //ms = 10s ! How long the ride last
    private RideType rideType; //what type this ride is
    private int exitement =80; //what exitement rate this ride has
    private int nausea = 10; //what nausea rate this ride has
    private boolean status=false; //is the ride open or not true=open
    private int broken=0; //how broken it is  100=broken 0=full repaired
    private int customerstotal=0; //how many customers total
    private float moneytotal=0; //how much money gained total
    private transient long lastGuestVisitTime=0; //how much time since last guest visited this ride
    private transient double guestRateHour=0; //how many guests per hour
    private int repairCost=100; //how much does it cost to repair this
    private String ride="you found a bug"; //this is variable used in saving
    
    //TODO:!! 
    
    private void calculateguestRate(){
        //TODO updateguestRate method that does this and modify this to just calculate the rate
        double a=System.currentTimeMillis()-lastGuestVisitTime; //aika jolloin laitteeseen tuli tybä
        double u=3600000/a; //tunti / a
        guestRateHour=u;
        lastGuestVisitTime=System.currentTimeMillis();
    }
    
    public BasicRide(MapPosition position,CustomAnimation object,List<Spatial> staticParts, float cost, Direction direction,String ride) {
        this.position = position;
        this.animatedPart = object;
        this.constructionmoney = cost;
        this.direction = direction;
        this.ride=ride;
        this.staticParts=staticParts;
        Main.injector.injectMembers(this);
        //Place it to the right place
        object.getObject().setLocalTranslation(position.getVector()); //temp fix
        for(Spatial s:staticParts){
            s.setLocalTranslation(position.getVector());
            logger.log(Level.FINEST,"Object {0} moved to {1}",new Object[]{s,s.getLocalTranslation()});
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

    }

    public void updateRide() {
        ArrayList<Guest>asd=new ArrayList<>(guestsInRide);
        List<Guest> guestscopy=(ArrayList<Guest>)asd.clone();
        for (Guest g : guestscopy) {
            if (g.getJoinedRide() + rideLength < System.currentTimeMillis()) {
                leaveRide(g);
            }
        }
    }

    public void takeQuestToRide(Guest g) {
        
        guestsInQue.remove(g);
        guestsInRide.add(g);
        g.setJoinedRide(System.currentTimeMillis());
        g.getGeometry().setLocalTranslation(position.getVector());
        g.getWallet().pay(price);
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
        //Put the guest to the enterance position
        int x1 = (int) (exit.getLocation().getX() + 0.4999);
        int y1 = (int) (exit.getLocation().getY() + 0.4999);
        int z1 = (int) (exit.getLocation().getZ() + 0.4999);
        g.getGeometry().setLocalTranslation(x1, y1, z1);
        g.initXYZ(x1, y1, z1);
        g.setActive(true);

    }
    public boolean toggleStatus(){
        status=!status;
        return status;
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
        node.detachChild(enterance.getObject());
        node.detachChild(exit.getObject());
        for(Spatial s: staticParts){
            node.detachChild(s);
        }
    }
    public ArrayList<Spatial> getAllSpatialsFromRide(){
        ArrayList<Spatial> list=new ArrayList<Spatial>(staticParts);
        if(exit!=null){
            list.add(exit.getObject());
        }
        if(enterance!=null){
            list.add(enterance.getObject());
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
    /*
     * GETTERS AND SETTERS.
     */
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
    
    public void setStats(int broken,int exitement,int nausea,boolean status){
        this.broken=broken;
        this.exitement=exitement;
        this.nausea=nausea;
        this.status=status;
    }

    public CustomAnimation getAnimatedPart() {
        return animatedPart;
    }

    public float getConstructionmoney() {
        return constructionmoney;
    }

    public int getCustomerstotal() {
        return customerstotal;
    }

    public Enterance getEnterance() {
        return enterance;
    }
    public Enterance getExit() {
        return exit;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Guest> getGuestsInQue() {
        return guestsInQue;
    }

    public List<Guest> getGuestsInRide() {
        return guestsInRide;
    }

    public long getLastGuestVisitTime() {
        return lastGuestVisitTime;
    }

    public float getMoneytotal() {
        return moneytotal;
    }

    public ShopReputation getReputation() {
        return reputation;
    }

    public List<Spatial> getStaticParts() {
        return staticParts;
    }
    public void setRide(String ride){
        this.ride=ride;
    }

    public void setAnimatedPart(CustomAnimation animatedPart) {
        this.animatedPart = animatedPart;
    }

    public void setBroken(int broken) {
        this.broken = broken;
    }

    public void setConstructionmoney(float constructionmoney) {
        this.constructionmoney = constructionmoney;
    }

    public void setCustomerstotal(int customerstotal) {
        this.customerstotal = customerstotal;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public RideType getRideType() {
        return rideType;
    }

    public void setEnterance(Enterance enterance) {
        this.enterance = enterance;
        this.enterance.setConnectedRide(this);
    }

    public void setExit(Enterance exit) {
        this.exit = exit;
        this.exit.setConnectedRide(this);
    }
    
}
