/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.npc.Guest;
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
}
