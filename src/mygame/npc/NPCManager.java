/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author arska
 */
public class NPCManager {
    public ArrayList<BasicNPC> npcs=new ArrayList<BasicNPC>();
    public ArrayList<Guest> guests=new ArrayList<Guest>();
    private Node rootNode;
    private Node NPCNode;
    public GuestSpawner guestSpawner;
    public boolean NPCVisible=true;
    int updatenum=0;
    
    public NPCManager(Node rootNode,AssetManager assetManager){
        this.rootNode=rootNode;
        NPCNode=new Node("NPCNode");
        guestSpawner=new GuestSpawner(NPCNode,rootNode,assetManager);
        rootNode.attachChild(NPCNode);
        
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
        updatenum++;
        
        if(updatenum%900==1){
            guestSpawner.forceSpawnGuest(updatenum);
        }
        if(npcs.isEmpty()==true){
            return;
        }
        for(BasicNPC npc:npcs){
            
            npc.update();
        }
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
    
}
