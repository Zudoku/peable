/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import static intopark.npc.BasicNPC.logger;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.roads.BuildingEnterance;
import intopark.roads.Road;
import intopark.roads.RoadGraph;
import intopark.roads.Walkable;
import intopark.util.Direction;
import intopark.util.MapPosition;
import intopark.terrain.ParkHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;

/**
 *
 * @author arska
 */
public class Guest extends BasicNPC {
    //DEPENDENCIES
    private  transient Random r;
    private transient ParkHandler parkHandler;
    //OWNS
    private List<Item> inventory = new ArrayList<>(); // What does the guest carry
    private StatManager stats = new StatManager(); //Emotions and feelings, thoughts
    private Wallet wallet; //Money situation
    //VARIABLES
    private transient GuestWalkingStates walkState = GuestWalkingStates.WALK; //Whether guest is walking or not
    private transient boolean active = true; //Is guest active AKA is he on ride? is he allowed to move
    private  transient long joinedRide;
    private boolean male;

    public Guest(Wallet wallet, int ID,double walkingSpeed, Direction moving,MapPosition position, StatManager stats, Spatial geom, String name,ParkHandler parkHandler) {
        super(name, geom,walkingSpeed,ID,position);
        this.parkHandler=parkHandler;
        this.moving = moving;
        this.stats = stats;
        this.wallet = wallet;
        r = new Random();
        super.getGeometry().setLocalScale((float) this.stats.getSize());
        super.getGeometry().setLocalTranslation(position.getVector());
        super.getGeometry().setUserData("type","guest");
        super.getGeometry().setUserData("ID",ID);
    }

    public void deleteActions() {
        actions.clear();
    }

    public boolean isEmptyActions() {
        if (actions.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (actions.size() < 1 && active) {
            calcMovePoints();
        }
        if (walkState == GuestWalkingStates.WALK) {
            super.move();
        }
        stats.update();
    }
    /**
     * We calculate our next Action (WHERE GUEST MOVES)
     */
    public void calcMovePoints() {
        boolean haveTarget=false;//When i want to expand to pathfinding
        boolean needTarget=false;
        if(needTarget){
            if(!haveTarget){
                //Pathfinding goes here...
                haveTarget=true;
            }
        }
        if(haveTarget){
            /* //Pathfinding goes here... */
        }else{
            /* Guest can move freely */
            RoadGraph roadGraph=parkHandler.getRoadGraph();
            /* Get the walkable that we are standing on */
            Walkable current=roadGraph.getWalkable(getPosition().getX(),getPosition().getY(),getPosition().getZ(),true);
            if(current==null){
                /* Not on road. Can't move */
                return;
            }
            DirectedGraph<Walkable,DefaultEdge>roadMap=roadGraph.getRoadMap();
            List<Walkable>possibilities=new ArrayList<>();
            /* Go through all possible directions */
            for(DefaultEdge edge:roadMap.outgoingEdgesOf(current)){
                Walkable possi=roadMap.getEdgeTarget(edge);
                possibilities.add(possi);
            }
            if(possibilities.isEmpty()){
                return;
            }
            int seed=0;
            /* Randomly select one direction */
            if(possibilities.size()>1){
                seed=r.nextInt(possibilities.size());
            }
            Walkable target=possibilities.get(seed);
            if(target instanceof Road){
                /* We need to know what direction we are moving */
                Direction roadDirection=getPosition().getDirection(target.getPosition().getX(),target.getPosition().getZ());
                if(moving.isOpposite(roadDirection)){
                    /* We don't want to go backwards if it isn't neccessary*/
                    if(5!=r.nextInt(10)){
                        /* Eventually goes backwards if there is a deadend. */
                        return;
                    }
                }
                Road road = (Road)target;
                if(road.getQueRoad()){
                    List<Walkable> queueRoads = new ArrayList<>();
                    Road handledRoad=road;
                    queueRoads.add(handledRoad);
                    queueRoads.add(current);
                    // Find out what is at the end of queroad.
                    boolean newWalkables = true;
                    findOtherWalkableLoop:
                    while(newWalkables){
                        for(DefaultEdge edge:roadGraph.getRoadMap().edgesOf(handledRoad)){
                            Walkable edgeSource = roadGraph.getRoadMap().getEdgeSource(edge);
                            //If new walkable
                            if(!queueRoads.contains(edgeSource)){
                                if(edgeSource instanceof Road){
                                    if(!((Road)edgeSource).getQueRoad()){
                                        //failed. No rides attached to this queroad
                                        logger.log(Level.FINER,"Found normal road at the end of queue.");
                                        return;
                                    }
                                    else{
                                        //Found another queroad repeat loop to it
                                        handledRoad = (Road)edgeSource;
                                        queueRoads.add(edgeSource);
                                        continue findOtherWalkableLoop;
                                    }
                                }
                                //If we found the destination
                                if(edgeSource instanceof BuildingEnterance){
                                    foundBuildingEnterance((BuildingEnterance)edgeSource);
                                    break findOtherWalkableLoop;
                                }
                            }
                            Walkable edgeTarget = roadGraph.getRoadMap().getEdgeTarget(edge);
                            if(!queueRoads.contains(edgeTarget)){
                                if(edgeTarget instanceof Road){
                                    if(!((Road)edgeTarget).getQueRoad()){
                                        //failed. No rides attached to this queroad
                                        logger.log(Level.FINER,"Found normal road at the end of queue.");
                                        return;
                                    }
                                    else{
                                        //Found another queroad repeat loop to it
                                        handledRoad = (Road)edgeTarget;
                                        queueRoads.add(edgeTarget);
                                        continue findOtherWalkableLoop;
                                    }
                                }
                                //If we found the destination
                                if(edgeTarget instanceof BuildingEnterance){
                                    foundBuildingEnterance((BuildingEnterance)edgeTarget);
                                    break findOtherWalkableLoop;
                                }
                            }

                        }
                        newWalkables = false;
                        logger.log(Level.FINER, "Didn't find anything interesting at the end of queue.");
                    }
                } else { //Not queroad.
                    walkToDirection(target, roadDirection);
                }
            }
            else if (target instanceof BuildingEnterance){
                BuildingEnterance ent=(BuildingEnterance) target;
                if(ent.getBuildingType()== BuildingEnterance.RIDE){
                    logger.log(Level.FINER, "Will not queue to ride because it has no queue");
                }
            }



        }
    }
    /**
     * This gets called when this guest finds BuildingEnterance at the end of queroad.
     * Determines what guest will do.
     * @param enterance Found BuildingEnterance.
     */
    private void foundBuildingEnterance(BuildingEnterance enterance){
        BuildingEnterance foundEnterance =enterance;
        int foundID = foundEnterance.getID();
        if (foundEnterance.getBuildingType() == BuildingEnterance.RIDE) {
            Object object = parkHandler.getObjectWithID(foundID);
            
            
        } else if (foundEnterance.getBuildingType() == BuildingEnterance.SHOP) {
            //TODO: Do queue logic for shops
        }
    }
    /**
     * Simple method to walk to given direction.
     * @param target target walkable.
     * @param roadDirection Direction we are heading.
     */
    private void walkToDirection(Walkable target,Direction roadDirection){
        /* WE WANT TO CREATE LANES SO WE COPY POSITION */
        MapPosition targetLocation = new MapPosition(target.getPosition());
        setOffsetLane(targetLocation, roadDirection);
        /* ADD THE ACTUAL ACTION TO ACTIONS */
        actions.add(getSimpleAction(targetLocation));
        moving = roadDirection;
    }
    private void setOffsetLane(MapPosition pos,Direction direction){
        float laneWidth=0.15f;
        if(direction== Direction.NORTH){
            pos.setOffSetZ(pos.getOffSetZ()+laneWidth);
        }
        if(direction== Direction.SOUTH){
            pos.setOffSetZ(pos.getOffSetZ()-laneWidth);
        }
        if(direction== Direction.EAST){
            pos.setOffSetX(pos.getOffSetX()+laneWidth);
        }
        if(direction== Direction.WEST){
            pos.setOffSetX(pos.getOffSetX()-laneWidth);
        }
    }
    public void callToQueue(Road queueroad) {
        Direction moveDirection = getPosition().getDirection(queueroad.getPosition().getX(),queueroad.getPosition().getZ());
        walkToDirection(queueroad, moveDirection);
    }
    public boolean doIWantToGoTo(Object place){
        return false;
    }
    public void forceMove(MapPosition destination){
        actions.add(getSimpleAction(destination));
    }
    /*
    public boolean doIWantToGo(BasicShop shop){
        return true;
    }
    public boolean doIWantToGoThere(BasicRide ride) {
        //TODO: REWORK

        int h = stats.getHappyness() / 5; //0-20
        int e = ride.getExitement();   //0-80
        int p = 0;                //0-20
        switch (stats.getPreferredRide()) {
            case LOW:
                if (ride.getRideType() == RideType.LOW) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.MEDIUM) {
                    p = 10;
                }
                break;

            case MEDIUM:
                if (ride.getRideType() == RideType.MEDIUM) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.LOW) {
                    p = 10;
                }
                if (ride.getRideType() == RideType.HIGH) {
                    p = 10;
                }

                break;

            case HIGH:
                if (ride.getRideType() == RideType.MEDIUM) {
                    p = 10;
                }
                if (ride.getRideType() == RideType.CRAZY) {
                    p = 5;
                }
                if (ride.getRideType() == RideType.HIGH) {
                    p = 20;
                }
                break;

            case CRAZY:
                if (ride.getRideType() == RideType.CRAZY) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.NAUSEA) {
                    p = 10;
                }

                break;

            case NAUSEA:
                if (ride.getRideType() == RideType.NAUSEA) {
                    p = 20;
                }
                if (ride.getRideType() == RideType.CRAZY) {
                    p = 10;
                }

                break;

        }

        //happyness+exitement+preference+40
        int rating = h + e + p + 40;
        logger.log(Level.FINEST,"Guest got rating of {0}",rating);
        if (rating > 100) {
            logger.log(Level.FINEST,"Guest is ready to accept the ride");
            return true;
        }
        return false;
    }
    * */
    /**
     * GETTERS AND SETTERS
     */

    public GuestWalkingStates getWalkState() {
        return walkState;
    }
    private NPCAction getSimpleAction(MapPosition position){
        NPCAction action=new NPCAction(position,ActionType.NOTHING,this,true);
        return action;
    }
    public StatManager getStats() {
        return stats;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public long getJoinedRide() {
        return joinedRide;
    }
    public void setJoinedRide(long joinedRide) {
        this.joinedRide = joinedRide;
    }
    public Wallet getWallet() {
        return wallet;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    public List<Item> getInventory() {
        return inventory;
    }

    public boolean isMale() {
        return male;
    }

}
