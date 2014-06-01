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
    public Identifier() {
        currentID=1;
    }
    public int reserveID(){
        currentID+=1;
        logger.log(Level.FINEST,"RESERVED ID TO YOU. CurrentID = {0} ID returned to you = {1}",new Object[]{currentID,currentID-1});
        return currentID-1;
    }
    public int addNewObject(Object value){
        idMap.put(currentID, value);
        currentID+=1;
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP. CurrentID = {2}",new Object[]{value.toString(),currentID -1,currentID});
        return currentID-1;
    }
    public void addOldObject(int ID,Object value){
        idMap.put(ID, value);
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP",new Object[]{value.toString(),ID});
    }
    public Map<Integer, Object> getIdMap() {
        return idMap;
    }
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
