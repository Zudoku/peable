/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

/**
 *
 * @author arska
 */
public enum RoadHill {
    DOWN,FLAT,UP;
    public boolean isOpposite(RoadHill hill){
        if(this==DOWN&&hill==UP){
            return true;
        }
        if(this==UP&&hill==DOWN){
            return true;
        }
        return false;
    }
}
