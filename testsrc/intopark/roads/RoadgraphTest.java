/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.terrain.Direction;
import intopark.terrain.MapPosition;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arska
 */
public class RoadgraphTest {
    
    public RoadgraphTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addRoad method, of class Roadgraph.
     */
    @Test
    public void testAddNullRoad() {
        Road road = null;
        Roadgraph instance = new Roadgraph();
        try{
            instance.addWalkable(road);
            fail("Should throw exception for null argument.");
        }catch(IllegalArgumentException e){
            
        }
        
    }
    
    @Test
    public void testConnectAttachedRoads() {
        Road road; 
        Roadgraph instance = new Roadgraph();
        road=new Road(new MapPosition(1, 6, 1), RoadHill.FLAT, 1, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        road=new Road(new MapPosition(2, 6, 1), RoadHill.FLAT, 2, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        DirectedGraph<Walkable,DefaultEdge> roadMap=instance.getRoadMap();
        assertSame("Should contain two vertexes.",2,roadMap.vertexSet().size());
        Walkable[] vertexes=roadMap.vertexSet().toArray(new Walkable[roadMap.vertexSet().size()]);
        assertNotNull(roadMap.getEdge(vertexes[0],vertexes[1]));
        assertNotNull(roadMap.getEdge(vertexes[1],vertexes[0]));
        assertNull(roadMap.getEdge(vertexes[1],vertexes[1]));
        
    }
    @Test
    public void testNotConnectAttachedRoads() {
        Road road; 
        Roadgraph instance = new Roadgraph();
        road=new Road(new MapPosition(1, 6, 1), RoadHill.FLAT, 1, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        road=new Road(new MapPosition(3, 6, 1), RoadHill.FLAT, 2, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        DirectedGraph<Walkable,DefaultEdge> roadMap=instance.getRoadMap();
        assertSame("Should contain two vertexes.",2,roadMap.vertexSet().size());
        Walkable[] vertexes=roadMap.vertexSet().toArray(new Walkable[roadMap.vertexSet().size()]);
        assertNull(roadMap.getEdge(vertexes[0],vertexes[1]));
        assertNull(roadMap.getEdge(vertexes[1],vertexes[0]));
        
    }
    @Test
    public void testSlopedRoads() {
        Road road; 
        Roadgraph instance = new Roadgraph();
        road=new Road(new MapPosition(1, 6, 1), RoadHill.FLAT, 1, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        road=new Road(new MapPosition(2, 6, 1), RoadHill.UP, 2, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        road=new Road(new MapPosition(3, 7, 1), RoadHill.FLAT, 3, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        DirectedGraph<Walkable,DefaultEdge> roadMap=instance.getRoadMap();
        assertSame("Should contain three vertexes.",3,roadMap.vertexSet().size());
        Walkable[] vertexes=roadMap.vertexSet().toArray(new Walkable[roadMap.vertexSet().size()]);
        assertNotNull(roadMap.getEdge(vertexes[0],vertexes[1]));
        assertNotNull(roadMap.getEdge(vertexes[1],vertexes[0]));
        
        assertNotNull(roadMap.getEdge(vertexes[1],vertexes[2]));
        assertNotNull(roadMap.getEdge(vertexes[2],vertexes[1]));
        
        assertNull(roadMap.getEdge(vertexes[2],vertexes[0]));
        assertNull(roadMap.getEdge(vertexes[0],vertexes[2]));
        
    }

}