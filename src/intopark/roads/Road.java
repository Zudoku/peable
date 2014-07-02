/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.jme3.math.Vector3f;
import intopark.util.Direction;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class Road extends Walkable{
    private transient static final Logger logger = Logger.getLogger(Road.class.getName());
    private RoadHill roadhill;
    private int ID;
    private int skin=1;
    private boolean queRoad=false;
    private Direction direction;

    public Road(MapPosition position, RoadHill roadhill, int ID,int skin,boolean queRoad,Direction direction) {
        super(position);
        this.roadhill = roadhill;
        this.ID = ID;
        this.skin=skin;
        this.queRoad=queRoad;
        this.direction=direction;
    }
    /**
     * Checks if road is physically cabable of connecting to walkable
     * @param road2
     * @return
     */
    public boolean canConnect(Walkable walk){
        if(walk==null){
            throw new NullPointerException();
        }
        if(walk instanceof Road){
            Road road2=(Road)walk;
            /* Make a transform of road2 */
            RoadTransformContainer rtc=new RoadTransformContainer(road2.getPosition(), road2.getDirection(), road2.getRoadhill());
            /* Get a map with all possible transforms that can connect to this */
            Map<RoadTransformContainer,Boolean> possible=getPossibleAttachmentPos();
            /* If it matches, can physically connect */
            for(RoadTransformContainer te:possible.keySet()){ //We cant do possible.containsKey because we use Direction.ANY
                if(te.match(rtc)){
                    return true;
                }
            }
            return false;
        }
        else if(walk instanceof BuildingEnterance){
            BuildingEnterance ent=(BuildingEnterance)walk;
            /* Make a transform representing ent as a flat road */
            RoadTransformContainer rtc=new RoadTransformContainer(ent.getPosition(),Direction.ANY,RoadHill.FLAT);
            /* Get a map with all possible transforms that can connect to this */
            Map<RoadTransformContainer,Boolean> possible=getPossibleAttachmentPos();
            /* If it matches, can physically connect */
            
            /*
            for(RoadTransformContainer te:possible.keySet()){ //We cant do possible.containsKey because we use Direction.ANY
                if(te.match(rtc)){
                    return true;
                }
            }
            * */

            /* Because we need to connect road that is ontop*/
            if(ent.getPosition().isSameMainCoords(position)&&roadhill==RoadHill.FLAT){
                return true;
            }
            return false;
        }else{
            return false;
        }

    }

    public Map<RoadTransformContainer,Boolean> getPossibleAttachmentPos(){
        Map<RoadTransformContainer,Boolean> map=new HashMap<>();
        switch(roadhill){
            case FLAT:
                for(RoadTransformContainer rtc:getFlatAttachment()){
                    map.put(rtc,true);
                }
                break;

            case UP:
                for(RoadTransformContainer rtc:getUpAttachment()){
                    map.put(rtc,true);
                }
        }
        return map;
    }
    private List<RoadTransformContainer> getFlatAttachment(){
        List<RoadTransformContainer> list=new ArrayList<>();
        /* Can connect to flat roads next to this */
        list.add(new RoadTransformContainer(position.plus(new MapPosition(1, 0, 0)), Direction.ANY,RoadHill.FLAT));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(0, 0, 1)), Direction.ANY,RoadHill.FLAT));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(-1, 0, 0)), Direction.ANY,RoadHill.FLAT));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(0, 0, -1)), Direction.ANY,RoadHill.FLAT));
        /* Can connect to uphill roads going up from this */
        list.add(new RoadTransformContainer(position.plus(new MapPosition(1, 0, 0)), Direction.NORTH,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(0, 0, 1)), Direction.EAST,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(-1, 0, 0)), Direction.SOUTH,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(0, 0, -1)), Direction.WEST,RoadHill.UP));
        /* Can connect to uphill roads going up to this */
        list.add(new RoadTransformContainer(position.plus(new MapPosition(1, -1, 0)), Direction.SOUTH,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(0, -1, 1)), Direction.WEST,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(-1, -1, 0)), Direction.NORTH,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(new MapPosition(0, -1, -1)), Direction.EAST,RoadHill.UP));
        return list;
    }
    private List<RoadTransformContainer> getUpAttachment(){
        List<RoadTransformContainer> list=new ArrayList<>();
        MapPosition forward=direction.directiontoPosition();
        MapPosition backward=direction.getOpposite().directiontoPosition();
        /* Can connect to flat roads on the up and bottom of this */
        list.add(new RoadTransformContainer(position.plus(forward).plus(new MapPosition(0, 1, 0)),Direction.ANY,RoadHill.FLAT));
        list.add(new RoadTransformContainer(position.plus(backward),Direction.ANY,RoadHill.FLAT));
        /* Can connect to uphill roads on the top and bottom of this */
        list.add(new RoadTransformContainer(position.plus(forward).plus(new MapPosition(0, 1, 0)),direction,RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(backward).plus(new MapPosition(0, -1, 0)),direction,RoadHill.UP));
        /* Can connect to opposite directioned uphill roads up and bottom of this */
        list.add(new RoadTransformContainer(position.plus(forward),direction.getOpposite(),RoadHill.UP));
        list.add(new RoadTransformContainer(position.plus(backward),direction.getOpposite(),RoadHill.UP));
        return list;
    }
    public int getID() {
        return ID;
    }

    public RoadHill getRoadhill() {
        return roadhill;
    }

    public int getSkin() {
        return skin;
    }

    public Vector3f getVector3f(){
        return position.getVector();
    }
    public boolean getQueRoad(){
        return queRoad;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPosition(MapPosition position) {
        this.position = position;
    }

    public void setQueRoad(boolean queRoad) {
        this.queRoad = queRoad;
    }

    public void setRoadhill(RoadHill roadhill) {
        this.roadhill = roadhill;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }




}
