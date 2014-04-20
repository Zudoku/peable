/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

import com.jme3.math.Vector3f;
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
public class MapPositionTest {
    
    public MapPositionTest() {
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

    @Test
    public void testGetVectorSimple() {
        MapPosition instance = new MapPosition(new Vector3f(5, 6, 7));
        Vector3f result = instance.getVector();
        assertEquals(5, result.x,0.001);
        assertEquals(6, result.y,0.001);
        assertEquals(7, result.z,0.001);

        assertEquals(5, instance.getX());
        assertEquals(6, instance.getY());
        assertEquals(7, instance.getZ());
    }
    @Test
    public void testGetVector(){
        MapPosition instance = new MapPosition(new Vector3f(5.5f,6.5f,7.5f));
        Vector3f result = instance.getVector();
        
        assertEquals(5.5, result.x,0.001);
        assertEquals(6.5, result.y,0.001);
        assertEquals(7.5, result.z,0.001);
        
        assertEquals(5, instance.getX());
        assertEquals(6, instance.getY());
        assertEquals(7, instance.getZ());
        
        assertEquals(0.5f, instance.getOffSetX(),0.001);
        assertEquals(0.5f, instance.getOffSetY(),0.001);
        assertEquals(0.5f, instance.getOffSetZ(),0.001);

    }
    @Test
    public void testIsNextTo(){
        MapPosition pos1=new MapPosition(1, 1, 1);
        MapPosition pos2=new MapPosition(2, 1, 1);
        MapPosition pos3=new MapPosition(2, 1, 2);
        assertTrue(pos1.isNextTo(pos2));
        assertTrue(pos2.isNextTo(pos3));
        assertFalse(pos1.isNextTo(pos3));
    }
}