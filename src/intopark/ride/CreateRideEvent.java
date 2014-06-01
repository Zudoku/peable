/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.jme3.scene.Spatial;
import intopark.ride.actualrides.Archeryrange;
import intopark.ride.actualrides.Blender;
import intopark.ride.actualrides.ChessCenter;
import intopark.ride.actualrides.HauntedHouse;
import intopark.ride.actualrides.PirateShip;
import intopark.ride.actualrides.Rotor;
import intopark.shops.BasicBuildables;
import intopark.util.Direction;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arska
 */
public class CreateRideEvent {
    private MapPosition position;
    private CustomAnimation animation;
    private BasicBuildables type;
    private List<Spatial> staticContent;
    private Direction direction;
    private String name;
    private int ID;
    private int broken;
    private int exitement;
    private int nausea;
    private boolean status;
    private float price;
    private Enterance enterance;
    private Enterance exit;
    
    public CreateRideEvent(MapPosition position,BasicBuildables type,Direction direction,String name,
            int ID,int broken,int exitement,int nausea,boolean status,float price,Enterance enterance,Enterance exit) {
        this.position = position;
        this.type=type;
        this.direction=direction;
        this.name=name;
        this.ID=ID;
        this.broken=broken;
        this.exitement=exitement;
        this.nausea=nausea;
        this.status=status;
        this.price=price;
        this.enterance=enterance;
        this.exit=exit;
        
        staticContent=new ArrayList<>();
    }           

    public BasicRide toRide(){
        BasicRide ride=null;
        //TODO: MAKE THIS A SWITCH
        if("chess".equals(type)){
            ride=new ChessCenter(position, animation,staticContent, direction);
        }
        else if("archery".equals(type)){
            ride=new Archeryrange(position, animation, staticContent, direction);
        }
        else if("blender".equals(type)){
            ride=new Blender(position, animation, staticContent, direction);
        }
        else if("hhouse".equals(type)){
            ride=new HauntedHouse(position, animation, staticContent, direction);
        }
        else if("rotor".equals(type)){
            ride=new Rotor(position, animation, staticContent, direction);
        }
        else if("pirateship".equals(type)){
            ride= new PirateShip(position, animation, staticContent, direction);
        }
        ride.setEnterance(enterance);
        ride.setExit(exit);
        ride.setName(name);
        ride.setID(ID);
        ride.setPrice(price);
        ride.setRide(type);
        ride.setStats(broken, exitement, nausea, status);
        ride.setAllSpatialsUserData("type","ride");
        ride.setAllSpatialsUserData("ID",ID);
        return ride;
    }
}
