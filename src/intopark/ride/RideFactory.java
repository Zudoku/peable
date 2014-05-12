/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.ride;

import com.google.inject.Inject;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
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
import intopark.util.Direction;
import intopark.util.MapPosition;

/**
 *
 * @author arska
 */
public class RideFactory {
    private static final Logger logger = Logger.getLogger(RideFactory.class.getName());
    
    AssetManager assetManager; 
    @Inject
    public RideFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    
    public ChessCenter chessCenter(Vector3f position,Direction facing){
        
        Spatial geom =assetManager.loadModel(LoadPaths.chess);
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        ChessCenter ride=new ChessCenter(new MapPosition(position),moving,staticContent,facing);
        return ride;
        
    }
    public Archeryrange archeryRange(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel(LoadPaths.archery);
        
        position.y +=0.1;
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        Archeryrange ride=new Archeryrange(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public Rotor rotor(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel(LoadPaths.rotor);
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        Rotor ride=new Rotor(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public PirateShip pirateShip(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel(LoadPaths.pirateCore);
        Spatial geom2=assetManager.loadModel(LoadPaths.pirateSwing);
        
        CustomAnimation moving=new CustomAnimation(geom2,AnimationType.ROLLH);
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        PirateShip ride=new PirateShip(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public SpinWheel spinWheel(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel(LoadPaths.spinwheelCore);
        Spatial geom2=assetManager.loadModel(LoadPaths.spinwheelSwing);
        
        CustomAnimation moving=new CustomAnimation(geom2,AnimationType.ROLLV);
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        SpinWheel ride=new SpinWheel(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public HauntedHouse hauntedHouse(Vector3f position,Direction facing){
        Spatial geom =assetManager.loadModel(LoadPaths.hauntedhouse);
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        HauntedHouse ride=new HauntedHouse(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public Blender blender(Vector3f position,Direction facing){
        Spatial geom =assetManager.loadModel(LoadPaths.blender);
        
        CustomAnimation moving=new CustomAnimation(geom, AnimationType.ROLLV);
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        
        Blender ride=new Blender(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    
    
}
