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
import mygame.ride.actualrides.ChessCenter;
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
        
        Spatial geom =assetManager.loadModel("Models/Rides/chesshouse.j3o");
        
        ChessCenter ride=new ChessCenter(position,geom,500,facing ,rootNode);
        return ride;
        
    }
}
