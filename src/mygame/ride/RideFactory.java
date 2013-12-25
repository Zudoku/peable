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
import mygame.ride.actualrides.Archeryrange;
import mygame.ride.actualrides.Blender;
import mygame.ride.actualrides.ChessCenter;
import mygame.ride.actualrides.HauntedHouse;
import mygame.ride.actualrides.PirateShip;
import mygame.ride.actualrides.Rotor;
import mygame.ride.actualrides.SpinWheel;
import mygame.terrain.Direction;

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
        
        ChessCenter ride=new ChessCenter(position,geom,500,facing ,rootNode);
        return ride;
        
    }
    public Archeryrange archeryRange(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/archeryrange/archeryrange.j3o");
        position.y +=0.1;
        Archeryrange ride=new Archeryrange(position, geom,facing,rootNode);
        return ride;
    }
    public Rotor rotor(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/Rotor/rotor.j3o");
        
        Rotor ride=new Rotor(position, geom,facing,rootNode);
        return ride;
    }
    public PirateShip pirateShip(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/PirateShip/core.j3o");
        Spatial geom2=assetManager.loadModel("Models/Rides/PirateShip/swing.j3o");
        PirateShip ride=new PirateShip(position, geom,facing,rootNode,geom2);
        return ride;
    }
    public SpinWheel spinWheel(Vector3f position,Direction facing){
        Spatial geom=assetManager.loadModel("Models/Rides/Spinwheel/core.j3o");
        Spatial geom2=assetManager.loadModel("Models/Rides/Spinwheel/swing.j3o");
        SpinWheel ride=new SpinWheel(position, geom,facing,rootNode,geom2);
        return ride;
    }
    public HauntedHouse hauntedHouse(Vector3f position,Direction facing){
        Spatial geom =assetManager.loadModel("Models/Rides/Hauntedhouse/hauntedhouse.j3o");
        
        HauntedHouse ride=new HauntedHouse(position,geom,370,facing ,rootNode);
        return ride;
    }
    public Blender blender(Vector3f position,Direction facing){
        Spatial geom =assetManager.loadModel("Models/Rides/Blender/blender.j3o");
        
        Blender ride=new Blender(position,geom,facing ,rootNode);
        return ride;
    }
}
