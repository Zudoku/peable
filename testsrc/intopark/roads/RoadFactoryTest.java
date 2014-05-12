/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import intopark.UtilityMethods;
import intopark.inout.LoadPaths;
import intopark.util.Direction;
import intopark.util.MapPosition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author arska
 */
public class RoadFactoryTest {
    
    public RoadFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        AssetManager b=Mockito.mock(AssetManager.class);
        Mockito.stub(b.loadModel(LoadPaths.roadstraight)).toReturn(new Node("Straight"));
        Mockito.stub(b.loadModel(LoadPaths.roaduphill)).toReturn(new Node("angle"));
        UtilityMethods a=new UtilityMethods(null, null,b );
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of roadToSpatial method, of class RoadFactory.
     */
    @Test
    public void testNullRoadToSpatial() {
        RoadFactory instance = new RoadFactory();
        /* First try null*/
        try {
            instance.roadToSpatial(null, new boolean[]{true, true, true, true});
            fail("Null road should not be let forward.");
        } catch (IllegalArgumentException e) {
        }
        /* Then try too few and too many arguments */
        try {
            instance.roadToSpatial(new Road(new MapPosition(1, 1, 1), RoadHill.UP, 6, 1, true, Direction.NORTH), new boolean[]{true, true});
            fail("Too few arguments should throw exeption.");
        } catch (IllegalArgumentException e) {
        }
        try {
            instance.roadToSpatial(new Road(new MapPosition(1, 2, 1), RoadHill.UP, 7, 1, true, Direction.NORTH), new boolean[]{true, true,true,false,false,false,false});
            fail("Too many arguments should throw exeption.");
        } catch (IllegalArgumentException e) {
        }
    }
    @Test
    public void testRoadToSpatial1(){
        RoadFactory instance = new RoadFactory();
        Road road=new Road(new MapPosition(5, 6, 3), RoadHill.UP, 8,1,false, Direction.SOUTH);
        Spatial roadSpatial=instance.roadToSpatial(road,new boolean[]{false,false,false,false});
        
        assertSame("road", roadSpatial.getUserData("type"));
        assertEquals(5f,roadSpatial.getLocalTranslation().x,0.005f);//X
        assertEquals(6f+0.5f,roadSpatial.getLocalTranslation().y,0.005f);//Y
        assertEquals(3f,roadSpatial.getLocalTranslation().z,0.005f);//Z
  
    }
    @Test
    public void testRoadToSpatial2(){
        RoadFactory instance = new RoadFactory();
        Road road=new Road(new MapPosition(2, 6, 6), RoadHill.FLAT, 8,1,false, Direction.SOUTH);
        //BOOLEAN NORTH-SOUTH-EAST-WEST
        
    }
}