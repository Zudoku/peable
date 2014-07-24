/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.google.common.collect.Lists;
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
import intopark.UtilityMethods;
import intopark.npc.Guest;
import intopark.npc.inspector.Inspection;
import intopark.npc.inventory.RideType;
import intopark.roads.Road;
import intopark.util.Direction;
import intopark.util.MapPosition;
import intopark.terrain.events.PayParkEvent;
import intopark.terrain.events.RideDemolishEvent;
import intopark.roads.RoadMaker;
import intopark.roads.RoadGraph;
import intopark.roads.Walkable;
import intopark.shops.BasicBuildables;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jgraph.graph.DefaultEdge;

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
    private int ID = 0; //every ride has its own id in parks
    private float price = 1; //charged from the getCustomersInRideSize every time they visit your ride
    private Direction direction; //not implemented yet
    private MapPosition position; //position of this ride
    private float constructionmoney = 0; //how much did it cost to build this building
    private String rideName = "You found a bug"; //rides name
    private Inspection lastInspection;
    private int rideLength = 10000; //ms = 10s ! How long the ride last
    private RideType rideType; //what type this ride is
    private int exitement =80; //what exitement rate this ride has
    private int nausea = 10; //what nausea rate this ride has
    private boolean status=false; //is the ride open or not true=open
    private int broken=0; //how broken it is  100=broken 0=full repaired
    private int customerstotal=0; //how many getCustomersInRideSize total
    private float moneytotal=0; //how much money gained total
    private transient long lastGuestVisitTime=0; //how much time since last guest visited this ride
    private transient double guestRateHour=0; //how many guests per hour
    private int repairCost=100; //how much does it cost to repair this
    private BasicBuildables ride; //this is variable used in saving
    /* INSPECTION */
    private boolean validInspection;

    //TODO:!!

    private void calculateguestRate(){
        //TODO updateguestRate method that does this and modify this to just calculate the rate
        double a=System.currentTimeMillis()-lastGuestVisitTime; //aika jolloin laitteeseen tuli tybä
        double u=3600000/a; //tunti / a
        guestRateHour=u;
        lastGuestVisitTime=System.currentTimeMillis();
    }

    public BasicRide(MapPosition position,CustomAnimation object,List<Spatial> staticParts, float cost, Direction direction,BasicBuildables ride) {
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
    /**
     * Asks the ride if it is ready take on guests to queue. Returns true if successful. Also takes the guest to the queue if true.
     * @param guest To be queue'd.
     * @return true if it was successfull.
     */
    public boolean tryToQueGuest(Guest guest) {
        if(status){
            if(validInspection){
                if(guestsInQue.size() >= getAllQueueRoadsInOrder().size()){ //FIXME
                    return false;
                }
                guestsInQue.add(guest);
                guest.setActive(false);
                return true;
            }else{
                //logger.log(Level.FINER, "Ride {0} can't take guests because it needs to be inspected first!",this.toString());
                return false;
            }

        }
        //logger.log(Level.FINER, "Ride {0} can't take guests because it needs to be opened first!",this.toString());
        return false;
    }
    /**
     * Handles the logic that tells the guests where they are supposed to be standing when they are queueing to a this ride.
     * Gets called on every frame.
     */
    public void updateQueLine() {
        if (guestsInQue.isEmpty()) {
            return;
        }
        for (int i = 0; i < guestsInQue.size(); i++) { //For every guest in the line.
            Guest handledguest = guestsInQue.get(i);
            if(!handledguest.getActions().isEmpty()){
                break;
            }
            List<Road> queue = getAllQueueRoadsInOrder();
            Walkable guestCurrentWalkable = roadMaker.getRoadGraph().getWalkable(handledguest.getPosition().getX(), handledguest.getPosition().getY(), handledguest.getPosition().getZ(), true);
            if(guestCurrentWalkable == null){
                //Guest is not standing on anything but still is in queue how is this possible?
                logger.log(Level.SEVERE, "Guest is not standing on anything but still is in queue.");
                throw new RuntimeException("Guest is not standing on anything but still is in queue.");
            }
            if(!(guestCurrentWalkable instanceof Road)){
                //Guest is standing on something other than road. This is not supposed to happen.
                logger.log(Level.SEVERE, "Guest is standing on something other than road while queueing to a ride.");
                throw new RuntimeException("Guest is standing on something other than road while queueing to a ride.");
            }
            Road guestCurrentRoad = (Road)guestCurrentWalkable;
            int indexof= queue.indexOf(guestCurrentRoad);
            if(indexof==-1){ //If not standing on queue, go through every queueroad up until destinationRoad (The road where guest is supposed to be)
                //Full
                Road destinationRoad = queue.get(i);
                List<Road> reversedQueue = Lists.reverse(queue);
                for(Road road:reversedQueue){
                    if(road != destinationRoad){
                        //command guest to walk there
                        handledguest.callToQueue(road);
                    }else{
                        //command guest to walk there but then stop for loop
                        handledguest.callToQueue(road);
                        break;
                    }
                }
            }else{
                //Part
                int currentPosition = queue.indexOf(guestCurrentRoad); //currentPosition is always higher or equal to i
                for(int p = currentPosition-1;p >= i;p--){//for loop doesn't get executed if guest is on the right place already.
                    Road r = queue.get(p);
                    handledguest.callToQueue(r);
                    //command guest to walk on r
                }
            }
        }
    }

    public void updateRide() {
        ArrayList<Guest>asd=new ArrayList<>(guestsInRide);
        List<Guest> guestscopy=(ArrayList<Guest>)asd.clone();
        for (Guest g : guestscopy) {
            if (g.getJoinedRide() + rideLength < System.currentTimeMillis()) {
                removeGuestFromRide(g);
            }
        }
    }
    /**
     * Returns all queueroads in order starting from the first being connected to enterance.
     * @return
     */
    private List<Road>getAllQueueRoadsInOrder(){
        if(enterance == null){
            throw new NullPointerException("Enterance is not placed yet. Cant find a queue.");
        }
        List<Road> queue = new ArrayList<>(); //The list to be returned.
        RoadGraph roadGraph=roadMaker.getRoadGraph();
        Road currentRoad;
        //Find the first road.
        Set edgeSet = roadGraph.getRoadMap().edgesOf(enterance.getEnteranceWalkable());
        if(edgeSet.isEmpty()){ //If there is no first road.
            return queue;
        }
        DefaultEdge edge = (DefaultEdge)edgeSet.toArray()[0];
        Walkable firstRoad=UtilityMethods.getTheOtherEndOfEdge(roadGraph, edge,enterance.getEnteranceWalkable());
        currentRoad = (Road)firstRoad;
        queue.add(currentRoad);
        //Find the rest of the roads.
        boolean finding = true;
        while(finding){
            //For all the outgoing edges.
            for (DefaultEdge edges : roadGraph.getRoadMap().edgesOf(currentRoad)) {
                //Find the other end
                Walkable foundWalkable = UtilityMethods.getTheOtherEndOfEdge(roadGraph, edges, currentRoad);
                //If we found a road
                if (foundWalkable instanceof Road) {
                    Road foundRoad = (Road)foundWalkable;
                    //But its not queroad.
                    if(!foundRoad.getQueRoad()){
                        //Find over.
                        finding = false;
                        //logger.log(Level.FINER, "Found the end of the queue.");
                        break;
                    }else{
                        //If we went backwards.
                        if(queue.contains(foundRoad)){
                            logger.log(Level.SEVERE, "Wrong way.");
                        }else {
                            //Found new queue road. Repeat the process to that.
                            currentRoad = foundRoad;
                            queue.add(currentRoad);
                            break;
                        }
                    }
                } else {
                    //Not a road?!?!? It could be possible that we went backwards on first road. Check if it is then skip else we have a malfunction.
                    //(Queueroad should never have 2 buildingEnterances attached.)
                    if(foundWalkable != enterance.getEnteranceWalkable()){
                        finding = false;
                        logger.log(Level.FINE, "Found another buildingEnterance stopping loop.");
                        break;
                    }
                }
            }
        }
        return queue;
    }
    /**
     * Gets called when guest gets inside the ride.
     * @param g
     */
    public void takeQuestToRide(Guest g) {
        //Move it to right list
        guestsInQue.remove(g);
        guestsInRide.add(g);
        //Start timer
        g.setJoinedRide(System.currentTimeMillis());
        //Move guest to physically be inside the ride. (might be buggy)
        g.getGeometry().setLocalTranslation(position.getVector());
        //Pay the ticket
        g.getWallet().pay(price);
        eventBus.post(new PayParkEvent(price));
        eventBus.post(new UpdateMoneyTextBarEvent());
        //Add to statistics
        calculateguestRate();
        customerstotal++;
        moneytotal += price;
        //Delete actions just in case
        g.deleteActions();
    }
    /**
     * Gets called when guest exits ride.
     * @param g
     */
    private void removeGuestFromRide(Guest g) {
        //delete guest from the list
        interact(g);
        guestsInRide.remove(g);
        //Put the guest to the exit position
        g.getGeometry().setLocalTranslation(exit.getLocation().getVector());
        //Add Action to move infront of the exit.
        MapPosition guestNewPosition = exit.getLocation();
        guestNewPosition = guestNewPosition.plus(exit.getDirection().directiontoPosition());
        g.forceMove(guestNewPosition);
        //Init XYZ to infront of exit.
        //g.setPosition(guestNewPosition);
        //Set guest to active again.
        g.setActive(true);

    }
    /**
     * Toggles if ride is open or not.
     * @return
     */
    public boolean toggleStatus(){
        status=!status;
        return status;
    }
    /**
     * Demolishes this ride.
     */
    public void demolish() {

        eventBus.post(new PayParkEvent(0.5f*constructionmoney));
        eventBus.post(new RideDemolishEvent(this));
        eventBus.post(new UpdateMoneyTextBarEvent());

    }
    /**
     * Update loop for this ride.
     */
    public void update(){
        runAnim();
        updateQueLine();
        updateRide();
        if(!guestsInQue.isEmpty()){
            Random r = new Random();
            if(r.nextInt(1000)==1){
                takeQuestToRide(guestsInQue.get(0));
            }
        }
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
    public List<Spatial> getAllSpatialsFromRide(boolean enterances){
        List<Spatial> list=new ArrayList<>(staticParts);
        if(exit!=null && enterances){
            list.add(exit.getObject());
        }
        if(enterance!=null && enterances){
            list.add(enterance.getObject());
        }
        if(animatedPart!=null){
            list.add(animatedPart.getObject());
        }
        return list;
    }
    public void setAllSpatialsUserData(String key,Object data){
        for(Spatial s:getAllSpatialsFromRide(true)){
            s.setUserData(key, data);
        }
    }
    /*
     * GETTERS AND SETTERS.
     */
    public Spatial getGeometry(){
        return animatedPart.getObject();
    }
    public int getCustomersInRideSize(){
        return guestsInRide.size();
    }
    public Vector3f getPositionVector(){
        return position.getVector();
    }
    public MapPosition getPosition(){
        return position;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
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

    public BasicBuildables getRide() {
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

    public Inspection getLastInspection() {
        return lastInspection;
    }

    public void setLastInspection(Inspection lastInspection) {
        this.lastInspection = lastInspection;
        if(lastInspection!=null){
            validInspection = lastInspection.isInspectionPassed();
        }
    }



    public List<Spatial> getStaticParts() {
        return staticParts;
    }

    public void setRide(BasicBuildables ride) {
        this.ride = ride;
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

    public boolean isValidInspection() {
        return validInspection;
    }
    
}
