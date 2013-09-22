/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.npc.Guest;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class BasicShop {
    public Direction facing;
    Vector3f position;
    Spatial object;
    public float constructionmoney=0;
    public String productname="productname";
    public String shopName="SHOPNAME";
    public float price=0;
    ArrayList<Employee> employees=new ArrayList<Employee>();
    
    public BasicShop(Vector3f position,Spatial object,float cost,Direction facing){
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
    
}
