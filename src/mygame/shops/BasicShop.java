/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Gamestate;
import mygame.Main;
import mygame.npc.Guest;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;

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
    }
    
    public void interact(Guest guest){
        
    }
    public Spatial getGeometry() {
        return object;
    }
    public void demolish(){
        
        ParkHandler parkHandler=Main.currentPark;
        Spatial[][][] map=parkHandler.getMap();
        Node shopNode=(Node)rootNode.getChild("shopNode");
        shopNode.detachChild(object);
        rootNode.detachChild(object);
        
        for(int y=0;y<25;y++){
            for(int x=0;x<parkHandler.getMapHeight();x++){
                for(int z=0;z<parkHandler.getMapWidth();z++){
                    if(map[x][y][z]!=null){
                        if(map[x][y][z]==object){
                            map[x][y][z]=null;
                        }
                    }
                }
            }
        }
        parkHandler.getParkWallet().add(0.5f*constructionmoney);
        Gamestate.ingameHUD.updateMoneytextbar();
        parkHandler.getShops().remove(this);
    }
    
}
