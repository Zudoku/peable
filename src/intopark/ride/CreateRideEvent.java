/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.jme3.scene.Spatial;
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
    private BasicBuildables type;
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


    public CreateRideEvent(MapPosition position,BasicBuildables type,Direction direction,int ID){
        this.position = position;
        this.type=type;
        this.direction=direction;
        this.ID=ID;
        this.name=type.toString()+" "+ID;
        this.broken=-1;
        this.exitement=-1;
        this.nausea=-1;
        this.price=5; //default price
        this.status=false;
    }
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
    }

    public BasicRide toRide(){
        BasicRide ride=null;

        RideFactory factory = new RideFactory();
        ride = factory.getRideFromBasicBuildable(type, position, direction);
        if(ride==null){
            throw new NullPointerException("Unable to transform CreateRideEvent to BasicRide.");
        }
        if(enterance!=null){
            ride.setEnterance(enterance);
        }
        if(exit!=null){
            ride.setExit(exit);
        }
        ride.setName(name);
        ride.setID(ID);
        ride.setPrice(price);
        ride.setRide(type);
        ride.setStats(broken, exitement, nausea, status);
        //ride.setAllSpatialsUserData("type","ride");
        //ride.setAllSpatialsUserData("ID",ID);
        return ride;
    }

    public Enterance getEnterance() {
        return enterance;
    }

    public Enterance getExit() {
        return exit;
    }

}
