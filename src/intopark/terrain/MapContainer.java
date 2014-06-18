/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import intopark.terrain.events.AddToBlockingMapEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class MapContainer {
    private transient static final Logger logger = Logger.getLogger(MapContainer.class.getName());
    private float[] mapData;
    private transient Map<String,Integer>blockingMap; // X,Y,Z -OPENS- ID
    private transient EventBus eventBus;
    @Inject
    public MapContainer(EventBus eventBus) {
        this.eventBus=eventBus;
        eventBus.register(this);
        blockingMap=new HashMap();
    }
    public void addToBlockingMap(int[] coords,int ID){
        if(coords.length==3){
            blockingMap.put(intArrayToKey(coords), ID);
        }else{
            logger.log(Level.FINER, "Coords should be X,Y,Z. So that length is 3");
        }
    }
    public void deleteWithKey(int[] keyArray){
        String key = intArrayToKey(keyArray);
        if(blockingMap.containsKey(key)){
            blockingMap.remove(key);
            logger.log(Level.FINEST, "Deleted key {0} from blockingMap.",key);
        }else{
            logger.log(Level.FINER, "Couldn't delete key {0} from blockingMap. No key found.",key);
        }
    }
    /**
     *
     * @param array
     * @return
     */
    private String intArrayToKey(int[]array){
        StringBuilder stringB=new StringBuilder(array[0]);
        for(int i:array){
            stringB.append("-").append(Integer.toString(i));
        }
        return stringB.toString();
    }
    public void deleteAllBlocking(int ID){
        int deleted=0;
        List<String> setcopy = new ArrayList<>(blockingMap.keySet());
        for(String key:setcopy){
            if(blockingMap.get(key).equals(ID)){
                blockingMap.remove(key);
                deleted++;
            }
        }
        logger.log(Level.FINEST, "Deleted {0} keys from blockingMap",deleted);
    }
    /**
     *
     * @param coords
     * @return | -2 = no blocking | -1 = no ID but blocks | ELSE return ID and blocks
     */
    public int checkIfBlocking(int[] coords){
        if(blockingMap.get(intArrayToKey(coords))!=null){
            return blockingMap.get(intArrayToKey(coords));
        }
        else{
            return -2;
        }
    }
    @Subscribe public void listenAddToBlockingMapEvent(AddToBlockingMapEvent event){
        addToBlockingMap(event.getCoords(), event.getID());
    }
    public void setMapData(float[]a){
        this.mapData=a;
    }
    public float[]getMapData(){
        return mapData;
    }
}
