/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import intopark.UtilityMethods;
import intopark.inout.LoadPaths;
import java.util.ArrayList;
import java.util.logging.Logger;
import intopark.ride.actualrides.Archeryrange;
import intopark.ride.actualrides.Blender;
import intopark.ride.actualrides.ChessCenter;
import intopark.ride.actualrides.HauntedHouse;
import intopark.ride.actualrides.PirateShip;
import intopark.ride.actualrides.Rotor;
import intopark.ride.actualrides.SpinWheel;
import intopark.shops.BasicBuildables;
import intopark.util.Direction;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class RideFactory {
    private static final Logger logger = Logger.getLogger(RideFactory.class.getName());

    public RideFactory() {
        
    }
    public BasicRide getRideFromBasicBuildable(BasicBuildables type,MapPosition position,Direction direction){
        switch(type){
            case ARCHERYRANGE:
                return archeryRange(position,direction);
            case CHESSCENTER:
                return chessCenter(position, direction);
            case ROTOR:
                return rotor(position, direction);
            case PIRATESHIP:
                return pirateShip(position, direction);
            case SPINWHEEL:
                return spinWheel(position, direction);
            case HAUNTEDHOUSE:
                return hauntedHouse(position, direction);
            case BLENDER:
                return blender(position, direction);
            default: return null;
        }
    }
    
    public ChessCenter chessCenter(MapPosition position,Direction direction){
        
        Spatial geom = UtilityMethods.loadModel(LoadPaths.chess);
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<>();
        staticContent.add(geom);
        
        ChessCenter ride=new ChessCenter(position,moving,staticContent,direction);
        return ride;
        
    }
    public Archeryrange archeryRange(MapPosition position,Direction direction){
        Spatial geom=UtilityMethods.loadModel(LoadPaths.archery);
        
        position.setOffSetY(position.getOffSetY()+0.1f);
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<>();
        staticContent.add(geom);
        
        Archeryrange ride=new Archeryrange(new MapPosition(position),moving,staticContent,direction);
        return ride;
    }
    public Rotor rotor(MapPosition position,Direction direction){
        Spatial geom=UtilityMethods.loadModel(LoadPaths.rotor);
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<>();
        staticContent.add(geom);
        
        Rotor ride=new Rotor(new MapPosition(position),moving,staticContent,direction);
        return ride;
    }
    public PirateShip pirateShip(MapPosition position,Direction direction){
        Spatial geom=UtilityMethods.loadModel(LoadPaths.pirateCore);
        Spatial geom2=UtilityMethods.loadModel(LoadPaths.pirateSwing);
        
        CustomAnimation moving=new CustomAnimation(geom2,AnimationType.ROLLH);
        ArrayList<Spatial>staticContent=new ArrayList<>();
        staticContent.add(geom);
        
        PirateShip ride=new PirateShip(new MapPosition(position),moving,staticContent,direction);
        return ride;
    }
    public SpinWheel spinWheel(MapPosition position,Direction direction){
        Spatial geom=UtilityMethods.loadModel(LoadPaths.spinwheelCore);
        Spatial geom2=UtilityMethods.loadModel(LoadPaths.spinwheelSwing);
        
        CustomAnimation moving=new CustomAnimation(geom2,AnimationType.ROLLV);
        ArrayList<Spatial>staticContent=new ArrayList<>();
        staticContent.add(geom);
        
        SpinWheel ride=new SpinWheel(new MapPosition(position),moving,staticContent,direction);
        return ride;
    }
    public HauntedHouse hauntedHouse(MapPosition position,Direction direction){
        Spatial geom =UtilityMethods.loadModel(LoadPaths.hauntedhouse);
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<>();
        staticContent.add(geom);
        
        HauntedHouse ride=new HauntedHouse(new MapPosition(position),moving,staticContent,direction);
        return ride;
    }
    public Blender blender(MapPosition position,Direction direction){
        Spatial geom =UtilityMethods.loadModel(LoadPaths.blender);
        
        CustomAnimation moving=new CustomAnimation(geom, AnimationType.ROLLV);
        ArrayList<Spatial>staticContent=new ArrayList<>();
        
        Blender ride=new Blender(new MapPosition(position),moving,staticContent,direction);
        return ride;
    }
    
    
}
