/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
    int enterancecount=0;
    

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

            case NULL:
                System.out.println("You just tried to buy null ride!");
                break;
        }
        int tx=(int)loc.x;
        int ty=(int)loc.y;
        int tz=(int)loc.z;
        Spatial[][][]map=Main.roadMaker.map;
        boughtride.rideID=rideID;
        boughtride.getGeometry().setUserData("rideID",rideID);
        boughtride.getGeometry().setUserData("type","ride");
        rides.add(boughtride);
        rideNode.attachChild(boughtride.getGeometry());
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                map[tx+i-1][ty][tz+j-1]=boughtride.getGeometry();
            }
        }
        rideID++;
        resetRidedata();
    }

    public void resetRidedata() {
        
        Main.shopManager.resetShopdata();
        Main.clickingHandler.clickMode= ClickingModes.RIDE;
        
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
    public void placeEnterance(Vector3f pos){
        
        int x =(int) ( pos.x - 0.4999f + 1);
        int y =(int) ( pos.y - 0.4999f + 1);
        int z =(int) ( pos.z - 0.4999f + 1);
        
        Spatial[][][] map=Main.roadMaker.map;
        
        boolean enterancetype=true;
        if(enterancecount==0){
            enterancetype=false;
        }
        
        if(map[x][y][z]!=null){
            return;    
        }
        if(map[x+1][y][z]!=null){
            Spatial s=map[x+1][y][z];
            if(!s.getUserData("type").equals("ride")){
                return;
            }
            int rideidArvo=s.getUserData("rideID");
            if(rideidArvo==rideID-1){
                
                
                Enterance e = new Enterance(enterancetype,new Vector3f(x, y, z), Direction.DOWN, assetManager);
                e.connectedRide=rides.get(rideID-2);  
                e.object.setUserData("type","enterance");
                map[x][y][z]=e.object;
                if(enterancetype==false){
                    rides.get(rideID-2).enterance=e;
                }else{
                    rides.get(rideID-2).exit=e;
                }
                
                rideNode.attachChild(e.object);
                enterancecount++;
                if(enterancecount>1){
                    enterancecount=0;
                    Main.clickingHandler.clickMode= ClickingModes.NOTHING;
                }
                return;
            }
        }
        if(map[x-1][y][z]!=null){
            Spatial s=map[x-1][y][z];
            if(!s.getUserData("type").equals("ride")){
                return;
            }
            int rideidArvo=s.getUserData("rideID");
            if(rideidArvo==rideID-1){
                Enterance e = new Enterance(enterancetype,new Vector3f(x, y, z), Direction.UP, assetManager);
                e.connectedRide=rides.get(rideID-2);
                e.object.setUserData("type","enterance");
                map[x][y][z]=e.object;
                rideNode.attachChild(e.object);
                enterancecount++;
                if(enterancecount>1){
                    enterancecount=0;
                    Main.clickingHandler.clickMode= ClickingModes.NOTHING;
                }
                return;
            }
        }
        if(map[x][y][z+1]!=null){
            Spatial s=map[x][y][z+1];
            if(!s.getUserData("type").equals("ride")){
                return;
            }
            int rideidArvo=s.getUserData("rideID");
            if(rideidArvo==rideID-1){
                Enterance e = new Enterance(enterancetype,new Vector3f(x, y, z), Direction.LEFT, assetManager);
                e.connectedRide=rides.get(rideID-2);
                e.object.setUserData("type","enterance");
                map[x][y][z]=e.object;
                rideNode.attachChild(e.object);
                enterancecount++;
                if(enterancecount>1){
                    enterancecount=0;
                    Main.clickingHandler.clickMode= ClickingModes.NOTHING;
                }
                return;
            }
        }
        if(map[x][y][z-1]!=null){
            Spatial s=map[x][y][z-1];
            if(!s.getUserData("type").equals("ride")){
                return;
            }
            int rideidArvo=s.getUserData("rideID");
            if(rideidArvo==rideID-1){
                Enterance e = new Enterance(enterancetype,new Vector3f(x, y, z), Direction.RIGHT, assetManager);
                e.connectedRide=rides.get(rideID-2);
                e.object.setUserData("type","enterance");
                map[x][y][z]=e.object;
                rideNode.attachChild(e.object);
                if(enterancecount>1){
                    enterancecount=0;
                    Main.clickingHandler.clickMode= ClickingModes.NOTHING;
                }
                enterancecount++;
            }
        }
        
    }
}
