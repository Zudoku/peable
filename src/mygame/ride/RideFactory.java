/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ride;

import com.google.inject.Inject;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.ride.actualrides.Archeryrange;
import mygame.ride.actualrides.Blender;
import mygame.ride.actualrides.ChessCenter;
import mygame.ride.actualrides.HauntedHouse;
import mygame.ride.actualrides.PirateShip;
import mygame.ride.actualrides.Rotor;
import mygame.ride.actualrides.SpinWheel;
import mygame.terrain.Direction;
import mygame.terrain.MapPosition;

/**
 *
 * @author arska
 */
public class RideFactory {
    AssetManager assetManager; 
    private final Node rootNode;
    @Inject
    public RideFactory(AssetManager assetManager,Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode=rootNode;
    }
    
    
    public ChessCenter chessCenter(Vector3f position,Direction facing){
        
        Spatial geom =assetManager.loadModel("Models/Rides/Chesshouse/chesshouse.j3o");
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        ChessCenter ride=new ChessCenter(new MapPosition(position),moving,staticContent,facing);
        return ride;
        
    }
    public Archeryrange archeryRange(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/archeryrange/archeryrange.j3o");
        
        position.y +=0.1;
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        Archeryrange ride=new Archeryrange(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public Rotor rotor(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/Rotor/rotor.j3o");
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        Rotor ride=new Rotor(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public PirateShip pirateShip(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/PirateShip/core.j3o");
        Spatial geom2=assetManager.loadModel("Models/Rides/PirateShip/swing.j3o");
        
        CustomAnimation moving=new CustomAnimation(geom2,AnimationType.ROLLH);
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        PirateShip ride=new PirateShip(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public SpinWheel spinWheel(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/Spinwheel/core.j3o");
        Spatial geom2=assetManager.loadModel("Models/Rides/Spinwheel/swing.j3o");
        
        CustomAnimation moving=new CustomAnimation(geom2,AnimationType.ROLLV);
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        SpinWheel ride=new SpinWheel(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public HauntedHouse hauntedHouse(Vector3f position,Direction facing){
        Spatial geom =assetManager.loadModel("Models/Rides/Hauntedhouse/hauntedhouse.j3o");
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        HauntedHouse ride=new HauntedHouse(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    public Blender blender(Vector3f position,Direction facing){
        Spatial geom =assetManager.loadModel("Models/Rides/Blender/blender.j3o");
        
        CustomAnimation moving=new CustomAnimation();
        ArrayList<Spatial>staticContent=new ArrayList<Spatial>();
        staticContent.add(geom);
        
        Blender ride=new Blender(new MapPosition(position),moving,staticContent,facing);
        return ride;
    }
    
    
}
