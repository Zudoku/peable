/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

import com.jme3.math.Vector3f;

/**
 *
 * @author arska
 */
public class MapPosition {
    private int x;
    private int y;
    private int z;
    private float offSetX;
    private float offSetY;
    private float offSetZ;
    
    
    public MapPosition(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public MapPosition(Vector3f vec){
        Vector3f vector=new Vector3f(vec);
        vector.x=vector.x-0.4999f;
        vector.y=vector.y-0.4999f;
        vector.z=vector.z-0.4999f;
        this.x=(int)vector.x;
        this.y=(int)vector.y;
        this.z=(int)vector.z;
        this.offSetX=(vec.x-this.x);
        this.offSetY=(vec.y-this.y);
        this.offSetZ=(vec.z-this.z);
        
    }
    public MapPosition(int x,int y,int z,float ox,float oy,float oz){
        this.x=x;
        this.y=y;
        this.z=z;
        this.offSetX=ox;
        this.offSetY=oy;
        this.offSetZ=oz;
    }
    public Vector3f getVector(){
        return new Vector3f(x+offSetX, y+offSetY, z+offSetZ);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
}
