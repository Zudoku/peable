/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain.decoration;

/**
 *
 * @author arska
 */
public class Decoration {
    public String x;
    public String z;
    public String y;
    public String type;
    public String direction;
    public Decoration(String x,String y,String z,String type,String direction){
        this.x=x;
        this.z=z;
        this.y=y;
        this.type=type;
        this.direction=direction;
    }
}
