/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author arska
 */
@Singleton
public class NPCManager {
    public ArrayList<BasicNPC> npcs;
    public ArrayList<Guest> guests=new ArrayList<Guest>();
    private Node rootNode;
    private Node NPCNode;
    public GuestSpawner guestSpawner;
    public boolean NPCVisible=true;
    int maxguests=20;
    @Inject
    public NPCManager(Node rootNode,AssetManager assetManager,EventBus eventBus){
        this.rootNode=rootNode;
        NPCNode=new Node("NPCNode");
        guestSpawner=new GuestSpawner(NPCNode,rootNode,assetManager,eventBus);
        rootNode.attachChild(NPCNode);
        eventBus.register(this);
        
    }
    public void addtoList(BasicNPC npc){
        npcs.add(npc);
        NPCNode.attachChild(npc.getGeometry());
    }
    public void deleteNPC(BasicNPC npc){
        NPCNode.detachChild(npc.getGeometry());
        npcs.remove(npc);
    }
    public void update(){
        
        
        if(maxguests>npcs.size()){
            Random r =new Random();
            if(r.nextInt(900)<=maxguests/guestSpawner.guestNum){
                guestSpawner.forceSpawnGuest(2);
            }
            
        }
        if(npcs.isEmpty()==true){
            System.out.println("NPCS EMPTY!!!!");
            
            return;
        }
        for(BasicNPC npc:npcs){
            
            npc.update();
        }
    }
    public void setMaxGuests(int a){
        this.maxguests=a;
    }
    
    //MISC ÄLÄ KÄYTÄ
    public void ToggleNPCVisible(){
        NPCVisible=!NPCVisible;
        if(NPCVisible==true){
            rootNode.attachChild(NPCNode);
        }else{
            rootNode.detachChild(NPCNode);
        }
    }
    public ArrayList<Guest> getGuests(){
        return guests;
    }
    @Subscribe public void listenAddGuestLimit(AddGuestLimitEvent event){
        this.maxguests=maxguests+event.getM();
    }
    
}
