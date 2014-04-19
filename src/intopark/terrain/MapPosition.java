/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

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
        vector.x=vector.x-0.4999f+1;
        vector.y=vector.y-0.4999f+1;
        vector.z=vector.z-0.4999f+1;
        this.x=(int)vector.x;
        this.y=(int)vector.y;
        this.z=(int)vector.z;
        this.offSetX=(vec.x-this.x);
        this.offSetY=(vec.y-this.y);
        this.offSetZ=(vec.z-this.z);
        
    }
    public MapPosition(MapPosition pos2){
        this.x=pos2.getX();
        this.y=pos2.getY();
        this.z=pos2.getZ();
        this.offSetX=pos2.getOffSetX();
        this.offSetY=pos2.getOffSetY();
        this.offSetZ=pos2.getOffSetZ();
    }
    public MapPosition(int x,int y,int z,float ox,float oy,float oz){
        this.x=x;
        this.y=y;
        this.z=z;
        this.offSetX=ox;
        this.offSetY=oy;
        this.offSetZ=oz;
    }
    public boolean isNextTo(MapPosition pos2){
        int deltaX=x-pos2.x;
        int deltaY=y-pos2.y;
        int deltaZ=z-pos2.z;
        /* We can't have bigger difference than 1 */
        if(deltaX>1||deltaX<-1){
            return false;
        }
        if(deltaZ>1||deltaZ<-1){
            return false;
        }
        /* We need to be on the same level */
        if(deltaY!=0){
            return false;
        }
        /* We cant be on the same location or be 2 away */
        if(deltaX!=0&&deltaY!=0||deltaX==0&&deltaY==0){
            return false;
        }
        return true;
    }
    public boolean isNextTo(MapPosition pos2,int x1,int z1,int y1){
        int deltaX=(x+x1)-pos2.x;
        int deltaY=(y+y1)-pos2.y;
        int deltaZ=(z+z1)-pos2.z;
        if(deltaX>1||deltaX<-1){
            return false;
        }
        if(deltaZ>1||deltaZ<-1){
            return false;
        }
        if(deltaY!=0){
            return false;
        }
        if(deltaX!=0&&deltaY!=0||deltaX==0&&deltaY==0){
            return false;
        }
        return true;
    }
    public boolean isSameMainCoords(MapPosition pos2){
        if(x!=pos2.getX()){
            return false;
        }
        if(y!=pos2.getY()){
            return false;
        }
        if(z!=pos2.getZ()){
            return false;
        }
        return true;
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

    public float getOffSetX() {
        return offSetX;
    }

    public float getOffSetY() {
        return offSetY;
    }

    public float getOffSetZ() {
        return offSetZ;
    }

    public void setOffSetX(float offSetX) {
        this.offSetX = offSetX;
    }

    public void setOffSetY(float offSetY) {
        this.offSetY = offSetY;
    }

    public void setOffSetZ(float offSetZ) {
        this.offSetZ = offSetZ;
    }
    
}
