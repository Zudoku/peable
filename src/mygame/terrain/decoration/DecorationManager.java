/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain.decoration;

import com.google.inject.Inject;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.terrain.Direction;
import mygame.terrain.ParkHandler;

/**
 *
 * @author arska
 */
public class DecorationManager {

    Node rootNode;
    Node decorationNode;
    Direction direction = Direction.UP;
    Decorations decoration = Decorations.ROCK;
    DecorationFactory decoFactory;
    private final ParkHandler parkHandler;

    @Inject
    public DecorationManager(Node rootNode, AssetManager assetManager,ParkHandler parkHandler) {
        this.rootNode = rootNode;
        this.parkHandler=parkHandler;
        decorationNode = new Node("decorationNode");
        rootNode.attachChild(decorationNode);
        decoFactory = new DecorationFactory(assetManager);

    }

    public void build(Vector3f loc) {
        Spatial decobject = null;
        float angle;
        switch (decoration) {
            case ROCK:

                break;
        }
        switch (direction) {
            case UP:
                angle = (float) Math.toRadians(90);
                decobject.rotate(0, angle, 0);
                break;

            case DOWN:
                angle = (float) Math.toRadians(90);
                decobject.rotate(0, angle, 0);
                break;

            case RIGHT:
                angle = (float) Math.toRadians(90);
                decobject.rotate(0, angle, 0);
                break;

            case LEFT:
                angle = (float) Math.toRadians(90);
                decobject.rotate(0, angle, 0);

        }
        decobject.setUserData("type", "decoration");
        decobject.setUserData("direction",direction);
        decobject.setLocalTranslation(loc);
        int x1=(int)loc.x;
        int y1=(int)loc.y;
        int z1=(int)loc.z;
        parkHandler.getMap()[x1][y1][z1]=decobject;
        decorationNode.attachChild(decobject);
    }
}
