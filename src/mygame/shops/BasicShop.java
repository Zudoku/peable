/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
import mygame.GUI.UpdateMoneyTextBarEvent;
import mygame.Main;
import mygame.npc.AddGuestLimitEvent;
import mygame.npc.Guest;
import mygame.terrain.Direction;
import mygame.terrain.PayParkEvent;

/**
 *
 * @author arska
 */
public class BasicShop {
    public Direction facing;
    public Vector3f position;
    private Spatial object;
    public int shopID=0;
    public float constructionmoney=0;
    public String productname="productname";
    public String shopName="SHOPNAME";
    public float price=0;
    
    
    @Inject protected EventBus eventBus;
    public ArrayList<Employee> employees=new ArrayList<Employee>();
    public ShopReputation reputation= ShopReputation.NEW;
    public String type;
    private final Node rootNode;
    
    public BasicShop(Vector3f position,Spatial object,float cost,Direction facing,Node rootNode){
        this.position=position;
        this.object=object;
        this.constructionmoney=cost;
        this.facing=facing;
        this.rootNode=rootNode;
        object.setLocalTranslation(position);
        Main.injector.injectMembers(this);
        Random r =new Random();
        eventBus.post(new AddGuestLimitEvent(r.nextInt(5)+5));
    }
    
    public void interact(Guest guest){
        
    }
    public Spatial getGeometry() {
        return object;
    }
    public void demolish(){

        eventBus.post(new ShopDemolishEvent(this));
        eventBus.post(new PayParkEvent(0.5f*constructionmoney));
        eventBus.post(new UpdateMoneyTextBarEvent());
        
    }
    
}
