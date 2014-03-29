/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.jme3.scene.Spatial;
import intopark.LoadPaths;
import intopark.UtilityMethods;
import intopark.ride.actualrides.Archeryrange;
import intopark.ride.actualrides.Blender;
import intopark.ride.actualrides.ChessCenter;
import intopark.ride.actualrides.HauntedHouse;
import intopark.ride.actualrides.PirateShip;
import intopark.ride.actualrides.Rotor;
import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arska
 */
public class CreateRideEvent {
    private MapPosition position;
    private CustomAnimation animation;
    private String type;
    private List<Spatial> staticContent;
    private Direction facing;
    private String name;
    private int rideID;
    private int broken;
    private int exitement;
    private int nausea;
    private boolean status;
    private float price;
    private Enterance enterance;
    private Enterance exit;
    public CreateRideEvent(MapPosition position,String type,Direction facing,String name,
            int rideID,int broken,int exitement,int nausea,boolean status,float price,Enterance enterance,Enterance exit) {
        this.position = position;
        this.type=type;
        this.facing=facing;
        this.name=name;
        this.rideID=rideID;
        this.broken=broken;
        this.exitement=exitement;
        this.nausea=nausea;
        this.status=status;
        this.price=price;
        this.enterance=enterance;
        this.exit=exit;
        
        staticContent=new ArrayList<Spatial>();
        /* Load models */
        if("chess".equals(type)){
            animation=new CustomAnimation();
            staticContent.add(UtilityMethods.loadModel(LoadPaths.chess));
        }
        else if("archery".equals(type)){
            animation=new CustomAnimation();
            staticContent.add(UtilityMethods.loadModel(LoadPaths.archery));
        }
        else if("blender".equals(type)){
            animation=new CustomAnimation();
            staticContent.add(UtilityMethods.loadModel(LoadPaths.blender));
        }
        else if("hhouse".equals(type)){
            animation=new CustomAnimation();
            staticContent.add(UtilityMethods.loadModel(LoadPaths.hauntedhouse));
        }
        else if("rotor".equals(type)){
            animation=new CustomAnimation();
            staticContent.add(UtilityMethods.loadModel(LoadPaths.rotor));
        }
        else if("pirateship".equals(type)){
            animation=new CustomAnimation(UtilityMethods.loadModel(LoadPaths.pirateSwing), AnimationType.ROLLH);
            staticContent.add(UtilityMethods.loadModel(LoadPaths.pirateCore));
        }
    }           

    public BasicRide toRide(){
        BasicRide ride=null;
        if("chess".equals(type)){
            ride=new ChessCenter(position, animation,staticContent, facing);
        }
        else if("archery".equals(type)){
            ride=new Archeryrange(position, animation, staticContent, facing);
        }
        else if("blender".equals(type)){
            ride=new Blender(position, animation, staticContent, facing);
        }
        else if("hhouse".equals(type)){
            ride=new HauntedHouse(position, animation, staticContent, facing);
        }
        else if("rotor".equals(type)){
            ride=new Rotor(position, animation, staticContent, facing);
        }
        else if("pirateship".equals(type)){
            ride= new PirateShip(position, animation, staticContent, facing);
        }
        ride.setEnterance(enterance);
        ride.setExit(exit);
        ride.setName(name);
        ride.setRideID(rideID);
        ride.setPrice(price);
        ride.setRide(type);
        ride.setStats(broken, exitement, nausea, status);
        ride.setAllSpatialsUserData("type","ride");
        ride.setAllSpatialsUserData("rideID",rideID);
        return ride;
    }
}
