/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
@Singleton
public class NPCManager {
    private static final Logger logger = Logger.getLogger(NPCManager.class.getName());
    private List<BasicNPC> npcs;
    private List<Guest> guests=new ArrayList<Guest>();
    private Node rootNode;
    private Node NPCNode;
    private GuestSpawner guestSpawner;
    private boolean NPCVisible=true;
    int maxguests=20;
    /**
     * Handles NPCs ingame
     * @param rootNode rootNode
     * @param assetManager AssetManager
     * @param eventBus EventBus
     */
    @Inject
    public NPCManager(Node rootNode,AssetManager assetManager,EventBus eventBus){
        this.rootNode=rootNode;
        NPCNode=new Node("NPCNode");
        guestSpawner=new GuestSpawner(NPCNode,assetManager,eventBus);
        rootNode.attachChild(NPCNode);
        eventBus.register(this);
        
    }
    /**
     * Add a guest to park NOTICE: npc is attached to NPCNode here!
     * @param npc guest to be added
     */

    /**
     * If you want to delete npc
     * @param npc npc to be deleted
     */
    public void deleteNPC(BasicNPC npc){
        NPCNode.detachChild(npc.getGeometry());
        npcs.remove(npc);
        logger.log(Level.FINEST, "Npc {0} has been deleted from the park!", npc.getName());
    }
    /**
     * Called every frame.
     * Updates Npcs.
     */
    public void update(){
        
        
        if(maxguests>npcs.size()){
            Random r =new Random();
            if(r.nextInt(900)<=maxguests/guestSpawner.getGuestNum()){
                guestSpawner.forceSpawnGuest(2);
            }
            
        }
        if(npcs.isEmpty()==true){
            return;
        }
        for(BasicNPC npc:npcs){
            
            npc.update();
        }
    }
    /**
     * Used by LoadManager when loading a game.
     * Sets GuestLimit.
     * @param a Value which is assigned to guestlimit
     */
    public void setMaxGuests(int a){
        this.maxguests=a;
    }
    
    /**
     * Toggles NPCs visible/invisible
     */
    public void ToggleNPCVisible(){
        NPCVisible=!NPCVisible;
        if(NPCVisible==true){
            rootNode.attachChild(NPCNode);
            logger.info("Toggled NPCs visible");
        }else{
            rootNode.detachChild(NPCNode);
            logger.info("Toggled NPCs invisible");
        }
        
    }
    /**
     * Get all guests in your park.
     * @return guests
     */
    public List<Guest> getGuests(){
        return guests;
    }
    @Subscribe public void listenAddGuestLimit(AddGuestLimitEvent event){
        this.maxguests=maxguests+event.getM();
        logger.finest("GuestLimit raised!");
    }

    public void setNpcs(List<BasicNPC> npcs) {
        this.npcs = npcs;
        guestSpawner.setNpcs(npcs);
    }

    public List<BasicNPC> getNpcs() {
        return npcs;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
        guestSpawner.setGuests(guests);
    }
    
    
    
}
