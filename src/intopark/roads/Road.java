/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.jme3.math.Vector3f;
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import java.util.logging.Level;
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
     * IGNORES QUEROADS
     * @param road2
     * @return 
     */
    public boolean canConnect(Road road2){
        int offsetY=0;
        if(roadhill==RoadHill.UP){
            offsetY=1;
        }
        if(roadhill==RoadHill.DOWN){
            offsetY=-1;
        }
        /* If road has an angle */
        if(offsetY!=0){
            /* If road2 is even on the right location to connect */
            if(position.isNextTo(road2.getPosition(),0,0,offsetY)){
                /* Check if road2 has an angle */
                if(road2.getRoadhill()!=RoadHill.FLAT){
                    /* If yes, then check if its going to same direction CASE 1*/
                    if(direction==road2.getDirection()){
                        /* check if it has same roadhill*/
                        if(roadhill==road2.getRoadhill()){
                            /* CASE 1 SUCCESS*/
                            //logger.log(Level.FINER,"CASE 1 SUCCESS");
                            return true;
                        }else{
                            /*CASE 1 FAILED*/
                            //logger.log(Level.FINER,"CASE 1 FAILED");
                            return false;
                        }
                    }else{
                        /*CASE 1 FAILED */
                        //logger.log(Level.FINER,"CASE 1 FAILED");
                        return false;
                    }
                }else{
                    /* road2 is flat CASE 2 SUCCESS*/
                    //logger.log(Level.FINER,"CASE 2 SUCCESS");
                    return true;
                }
                
            }/* check CASE 3*/
             else if (position.isNextTo(road2.getPosition())){
                /* Check if same direction and opposite roadhill OR same roadhill and opposite direction */
                if(checkCase3(this, road2)){
                    /* CASE 3 SUCCESS*/
                    //logger.log(Level.FINER,"CASE 3 SUCCESS");
                    return true;
                }else{
                    /* CASE 3 FAILED*/
                    //logger.log(Level.FINER,"CASE 3 FAILED");
                    return false;
                }
            }else{
                /* Not even next to each other CASES 1-3 FAILED */
                 //logger.log(Level.FINER,"CASES 1-3 FAILED");
                return false;
            }
        }else{
            /* road1 FLAT Check if next to eachother*/
            if(position.isNextTo(road2.getPosition())){
                /* Check if road2 has angle */
                if(road2.getRoadhill()!=roadhill.FLAT){
                    /* check if CASE 4 */
                    if(checkCase4(this, road2)){
                        /* CASE 4 SUCCESS */
                        //logger.log(Level.FINER,"CASE 4 SUCCESS");
                        return true;
                    }else{
                        /* CASE 4 FAILED */
                        //logger.log(Level.FINER,"CASE 4 FAILED");
                        return false;
                    }
                }else{
                    /* road2 no angle CASE 5 SUCCESS */
                    //logger.log(Level.FINER,"CASE 5 SUCCESS");
                    return true;
                }
            }else{
                /* Not even next to each other CASES 4-5 FAILED */
                //logger.log(Level.FINER,"CASES 4-5 FAILED");
                return false;
            }
        }

    }/**
     * CHECK CASE 3 DRAWING
     * @param road1
     * @param road2
     * @return 
     */
    private boolean checkCase3(Road road1,Road road2){
        /* Check if same direction and opposite roadhill */
        if(road1.getDirection()==road2.getDirection()&&road1.getRoadhill().isOpposite(road2.getRoadhill())){
            return true;
        }
        /* Check if same roadhill and opposite direction */
        if(road1.getRoadhill()==(road2.getRoadhill())&&road1.getDirection().isOpposite(road2.getDirection())){
            return true;
        }
        return false;
    }
    private boolean checkCase4(Road road1,Road road2){
        /* IF X1 < X2 OR X1 > X2 */
        if(road1.getPosition().getX()>road2.getPosition().getX()||road1.getPosition().getX()<road2.getPosition().getX()){
            if(road2.getDirection()==direction.NORTH||road2.getDirection()==direction.SOUTH){
                return true;
            }
        }
        /* IF Z1 < Z2 OR Z1 > Z2 */
        if(road1.getPosition().getZ()>road2.getPosition().getZ()||road1.getPosition().getZ()<road2.getPosition().getZ()){
            if(road2.getDirection()==direction.WEST||road2.getDirection()==direction.EAST){
                return true;
            }
        }
        return false;
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

    public MapPosition getPosition() {
        return position;
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