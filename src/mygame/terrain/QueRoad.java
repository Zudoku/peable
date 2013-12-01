/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

/**
 *
 * @author arska
 */
public class QueRoad extends Road{
    public String queconnect1;
    public String queconnect2;
    public String connected;
    public QueRoad(String x, String z, String y, String roadhill, String ID,String queconnect1,String queconnect2,String connected) {
        super(x, z, y, roadhill, ID);
        this.queconnect1=queconnect1;
        this.queconnect2=queconnect2;
        this.connected=connected;
    }
    
}
