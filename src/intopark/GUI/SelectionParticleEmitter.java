/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import intopark.UtilityMethods;
import intopark.inputhandler.ClickingHandler;
import intopark.shops.HolomodelDrawer;
import intopark.roads.RoadMaker;
import intopark.terrain.TerrainHandler;

/**
 *
 * @author arska
 */
@Singleton
public class SelectionParticleEmitter {
    //LOGGER
    //DEPENDENCIES
    private ClickingHandler clickingHandler;
    //VARIABLES
    private final Node rootNode;
    private final TerrainHandler terrainHandler;
    private HolomodelDrawer holoDrawer;
    @Inject
    public SelectionParticleEmitter(HolomodelDrawer holomodelDrawer,AssetManager aM,ClickingHandler click,RoadMaker roadMaker, Node rootNode, TerrainHandler worldHandler) {
        this.rootNode = rootNode;
        this.terrainHandler = worldHandler;
        this.clickingHandler=click;
        this.holoDrawer=holomodelDrawer;
    }

    public void updateSelection() {


        CollisionResults results = new CollisionResults();
        UtilityMethods.rayCast(results, rootNode);

        CollisionResult target = results.getClosestCollision();
        switch (clickingHandler.getClickMode()) {
            case TERRAIN:
                if (target == null) {
                    return;
                }
                if (!terrainHandler.getLocked()) {
                    terrainHandler.drawBrush(target.getContactPoint().x, target.getContactPoint().z);
                }

                break;
            case ROAD:
                break;
            case NOTHING:
                break;

            case PLACE:
                if (target == null) {
                    return;
                }
                holoDrawer.updateLocation(target);
                break;

        }

    }
}
