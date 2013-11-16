/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Main;
import mygame.npc.ActionType;
import mygame.npc.Guest;
import mygame.npc.NPCAction;
import mygame.shops.Employee;
import mygame.shops.ShopReputation;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class BasicRide {
    public Direction facing;
    public Vector3f position;
    private Spatial object;
    
    public int rideID=0;
    public float price=0;
    public float constructionmoney=0;
    public String rideName="RIDENAME";
    public Enterance enterance;
    public Enterance exit;
    public ArrayList<Employee> employees=new ArrayList<Employee>();
    public ShopReputation reputation= ShopReputation.NEW;
    public ArrayList<Guest> guestsInRide=new ArrayList<Guest>();
    public ArrayList<Guest> guestsInQue=new ArrayList<Guest>();
    //TODO!! 
    private boolean[][] occupySpace={
        {false,false,false,false},
        {false,false,false,false},
        {false,false,false,false},
        {false,false,false,false},
        
    };
    
    public BasicRide(Vector3f position,Spatial object,float cost,Direction facing){
        this.position=position;
        this.object=object;
        this.constructionmoney=cost;
        this.facing=facing;
        object.setLocalTranslation(position);
    }
    
    public void interact(Guest guest){
        
    }
    public Spatial getGeometry() {
        return object;
    }
    public boolean[][] getOccupySpace() {
        return occupySpace;
    }
    public boolean tryToQueGuest(Guest guest){
        guestsInQue.add(guest);
        
        return true;
    }
    public void updateQueLine(){
        if(guestsInQue.isEmpty()){
            return;
        }
        
        for(int i=0;i<guestsInQue.size();i++){
            Guest handledguest=guestsInQue.get(i);
            Spatial testedroad=handledguest.currentQueRoad;
            if(testedroad==null){
                System.out.println("Road from guest null :(");
            }
            ArrayList<Spatial> linkedroads=Main.roadMaker.getlinkedqueroads(enterance.connectedRoad);
            if(!linkedroads.contains(testedroad)){
                System.out.println("Error");
                continue;
            }
            int a=linkedroads.indexOf(testedroad);
            if(i==a){
                //Guest is where he is supposed to be
                
                if(i==0){
                    //ota sisään laitteeseen
                }
                continue;
            }
            if(i<a){
                //anna käsky eteenpoäin
                Vector3f newpos=linkedroads.get(i).getLocalTranslation();
                handledguest.callToQueRoad(new NPCAction(newpos, ActionType.QUE, handledguest));
                handledguest.currentQueRoad=linkedroads.get(i);
            }
        }
    }
}
