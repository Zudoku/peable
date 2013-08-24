/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Main;

/**
 *
 * @author arska
 */
public class Guest extends BasicNPC{
    
    private float money=10;
    private int guestnum;
    private int hunger=0;
    private int thirst=0;
    private int bathroom=0;
    private int x;
    private int y;
    private int z;
    private GuestWalkingStates walkState= GuestWalkingStates.WALK;
    Spatial[][][]roads;
    ArrayList<Vector3f> movePoints=new ArrayList<Vector3f>();
    
    
    public Guest(String name,float money,int guestNum,Spatial geom){
        super(name, geom);
       
        
        this.money=money;
        this.guestnum=guestNum;
                
    }

    @Override
    public void update() {
        if(movePoints.size()<3){
            calcMovePoints();
        }
        if(walkState==GuestWalkingStates.WALK){
            super.move(movePoints.get(0),movePoints);
        }
        
    }
    public void randomizeStats(){
        //todo
    }
    public void calcMovePoints(){
        Vector3f point=new Vector3f(10, 6, 4);
        movePoints.add(point);
        point=new Vector3f(4, 6, 10);
        movePoints.add(point);
    }
    public void initXYZ(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
        roads=Main.roadMaker.roads;
    }
    
}
