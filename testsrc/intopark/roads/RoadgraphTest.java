/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import intopark.util.Direction;
import intopark.util.MapPosition;
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
     * Test of addRoad method, of class RoadGraph.
     */
    @Test
    public void testAddNullRoad() {
        Road road = null;
        RoadGraph instance = new RoadGraph();
        try{
            instance.addWalkable(road);
            fail("Should throw exception for null argument.");
        }catch(IllegalArgumentException e){
            
        }
        
    }
    
    @Test
    public void testConnectAttachedRoads() {
        Road road; 
        RoadGraph instance = new RoadGraph();
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
        RoadGraph instance = new RoadGraph();
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
        RoadGraph instance = new RoadGraph();
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
    @Test
    public void testIncomingEdges(){
        Road road; 
        RoadGraph instance = new RoadGraph();
        road=new Road(new MapPosition(1, 6, 1), RoadHill.FLAT, 1, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        Road road2=new Road(new MapPosition(2, 6, 1), RoadHill.UP, 2, 1, false, Direction.NORTH);
        instance.addWalkable(road2);
        road=new Road(new MapPosition(3, 7, 1), RoadHill.FLAT, 3, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        DirectedGraph<Walkable,DefaultEdge> roadMap=instance.getRoadMap();
        for(DefaultEdge e:roadMap.edgesOf(road2)){
            assertNotNull(roadMap.getEdgeSource(e));
            assertNotNull(roadMap.getEdgeTarget(e));
        }
    }
    @Test
    public void testBendingEdge(){
        Road road; 
        RoadGraph instance = new RoadGraph();
        road=new Road(new MapPosition(1, 6, 1), RoadHill.FLAT, 1, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        Road road2=new Road(new MapPosition(2, 6, 1), RoadHill.FLAT, 2, 1, false, Direction.NORTH);
        instance.addWalkable(road2);
        Road road3=new Road(new MapPosition(2, 6, 2), RoadHill.FLAT, 3, 1, false, Direction.NORTH);
        instance.addWalkable(road3);
        DirectedGraph<Walkable,DefaultEdge> roadMap=instance.getRoadMap();
        Walkable[] vertexes=roadMap.vertexSet().toArray(new Walkable[roadMap.vertexSet().size()]);
        assertSame(2,roadMap.edgesOf(road).size());
        assertSame(2*2,roadMap.edgesOf(road2).size());
        assertSame(2,roadMap.edgesOf(road3).size());
        
        
    }
    
    @Test
    public void testRoadConnectToBuilding(){
        RoadGraph instance = new RoadGraph();
        //TEST THAT ROAD CONNECTS TO BUILDING
        Road road=new Road(new MapPosition(1, 6, 1), RoadHill.FLAT, 1, 1, false, Direction.NORTH);
        instance.addWalkable(road);
        BuildingEnterance ent= new BuildingEnterance(new MapPosition(2, 6, 1), 1, BuildingEnterance.SHOP);
        instance.addWalkable(ent);
        assertSame(2,instance.getRoadMap().edgesOf(ent).size());
        //TEST THAT RIDES DONT ACCEPT NORMAL ROAD
        Road road2=new Road(new MapPosition(1, 6, 5), RoadHill.FLAT, 2, 1, false, Direction.NORTH);
        instance.addWalkable(road2);
        BuildingEnterance ent2= new BuildingEnterance(new MapPosition(2, 6, 5), 1, BuildingEnterance.RIDE);
        instance.addWalkable(ent2);
        assertSame(0,instance.getRoadMap().edgesOf(ent2).size());
        //TEST THAT ROAD CONNECTS TO BUILDING WHILE SAME COORDS
        Road road3=new Road(new MapPosition(1, 6, 10), RoadHill.FLAT, 3, 1, false, Direction.NORTH);
        instance.addWalkable(road3);
        BuildingEnterance ent3= new BuildingEnterance(new MapPosition(1, 6, 10), 1, BuildingEnterance.SHOP);
        instance.addWalkable(ent3);
        assertSame(2,instance.getRoadMap().edgesOf(ent3).size());
        
    }
    
}