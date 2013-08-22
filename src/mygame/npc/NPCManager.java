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
    private ArrayList<BasicNPC> npcs=new ArrayList();
    Node rootNode;
    Node NPCNode;
    GuestSpawner guestSpawner;
    public boolean NPCVisible=true;
    int updatenum=0;
    
    public NPCManager(Node rootNode,AssetManager assetManager){
        this.rootNode=rootNode;
        NPCNode=new Node("NPCNode");
        guestSpawner=new GuestSpawner(NPCNode,rootNode,assetManager,npcs);
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
        
        if(updatenum%300==1){
            guestSpawner.forceSpawnGuest();
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
    
}
