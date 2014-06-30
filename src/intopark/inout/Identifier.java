/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class Identifier {
    private static final Logger logger = Logger.getLogger(Identifier.class.getName());
    private transient Map<Integer,Object> idMap=new HashMap<>();
    private int currentID;
    /**
     * Identifier is ID-mapping component. It should be used as a way to search entities with ID.
     * This way you can store just a reference to the real thing.
     * Example use is following.
     * 1. Reserve ID for new spawning guest.
     * 2. Set the ID to guests spatial as userdata.
     * 3. Add guest with addOldObject to the idMap.
     * 4. When clicking guest-spatial you can use getUserData to receive the ID.
     * 5. Look up the real guest with the ID using getObjectWithID.
     *
     * You don't have to store the whole guest in Spatial.setUserData.
     */
    public Identifier() {
        currentID=1;
    }
    /**
     * Reserves ID for you. It is used to identify the object later.
     * Remember to add the object to idMap so it can be looked up.
     * @return unique int.
     */
    public int reserveID(){
        currentID+=1;
        logger.log(Level.FINEST,"RESERVED ID TO YOU.YOUR ID IS {1}",new Object[]{currentID,currentID-1});
        return currentID-1;
    }
    /**
     * This is a straight forward way of reserving ID and adding it later on.
     * This reserves ID to you, then adds it to idMap and returns you the ID that object was assigned to.
     * @param value Object to assign.
     * @return ID that was used to assign value.
     */
    public int addNewObject(Object value){
        idMap.put(currentID, value);
        currentID+=1;
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP. CurrentID = {2}",new Object[]{value.toString(),currentID -1,currentID});
        return currentID-1;
    }
    /**
     * Add value to idMap using ID.
     * @param ID unique ID that was reserved from reserveID()
     * @param value Object to assign.
     */
    public void addOldObject(int ID,Object value){
        idMap.put(ID, value);
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP",new Object[]{value.toString(),ID});
    }
    public Map<Integer, Object> getIdMap() {
        return idMap;
    }
    /**
     * Attempts to return Object that is assigned to that ID.
     * Returns null if there is no Object assigned with that ID.
     * @param ID unique ID.
     * @return Object.
     */
    public Object getObjectWithID(int ID){
        Object object=null;
        if(idMap.containsKey(ID)){
            object=idMap.get(ID);
        }
        return object;
    }
    public int getCurrentID() {
        return currentID;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }
}
