/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import mygame.Main;
import mygame.inputhandler.ClickingModes;
import mygame.shops.BasicBuildables;
import mygame.terrain.Direction;

/**
 *
 * @author arska
 */
public class RideManager {
   
    public ArrayList<BasicRide> rides = new ArrayList<BasicRide>();
    RideFactory rideFactory;
    private AssetManager assetManager;
    public Node rideNode;
    public Node rootNode;
    int rideID=1;
    

    public RideManager(AssetManager assetManager,Node rootNode) {
        this.rideFactory= new RideFactory(assetManager);
        this.assetManager=assetManager;
        this.rootNode=rootNode;
        rideNode=new Node("rideNode");
        rootNode.attachChild(rideNode);
        
        

    }

    public void buy(Direction facing,BasicBuildables selectedBuilding) {
        Vector3f loc = Main.holoDrawer.pyorista(Main.holoDrawer.getLocation());
        BasicRide boughtride = null;
        
        
        
        switch (selectedBuilding) {
            case CHESSCENTER:
                boughtride=rideFactory.chessCenter(loc, facing);
                break;

            case MBALL:
               //change
                break;
                
            case ENERGY:
                //change
                break;
                
            case NULL:
                System.out.println("You just tried to buy null ride!");
                break;
        }
        boughtride.rideID=rideID;
        boughtride.getGeometry().setUserData("rideID",rideID);
        rides.add(boughtride);
        rideNode.attachChild(boughtride.getGeometry());
        
    }

    public void resetRidedata() {
        
        
        Main.shopManager.resetShopdata();
    }
    @Deprecated
    public BasicRide isthereRide(int x,int y ,int z){
        BasicRide b=null;
        if(rides.isEmpty()==false){
            for(BasicRide p:rides){
                int tx=(int)Main.holoDrawer.pyorista(p.position).x;
                int ty=(int)Main.holoDrawer.pyorista(p.position).y;
                int tz=(int)Main.holoDrawer.pyorista(p.position).z;
                if(tx==x&&ty==y&&tz==z){
                    b=p;
                    System.out.println("RIDE IS LOCATED!");
                    return b;
                    
                }
            }
        }
        return b;
    }
}
