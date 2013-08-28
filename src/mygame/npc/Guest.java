/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;
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
    private Random r;
    private GuestWalkingStates walkState= GuestWalkingStates.WALK;
    Spatial[][][]roads;
    ArrayList<Vector3f> movePoints=new ArrayList<Vector3f>();
    
    
    public Guest(String name,float money,int guestNum,Spatial geom){
        super(name, geom);
       
        Node test=new Node();
        
        this.money=money;
        this.guestnum=guestNum;
        r=new Random();
                
    }

    @Override
    public void update() {
        if(movePoints.size()<1){
            
            calcMovePoints();
        }
        if(walkState==GuestWalkingStates.WALK){
            if(roads[x][y][z]==null){
                return;
            }
            if(movePoints.isEmpty()==true){
                return;
            }
            super.move(movePoints.get(0),movePoints);
        }
        
    }
    public void randomizeStats(){
        //todo
    }
    public void calcMovePoints(){
        //0 p 1 e 2 i 3 l
        int suunta=r.nextInt(3);
        if(suunta==0){
            if(roads[x+1][y][z]!=null){
                movePoints.add(new Vector3f(x+1f, y+0.1f, z));
                x=x+1;
            }
            return;
        }
        if(suunta==1){
            if(roads[x-1][y][z]!=null){
                movePoints.add(new Vector3f(x-1, y+0.1f, z));
                x=x-1;
            }
            return;
        }
        if(suunta==2){
            if(roads[x][y][z+1]!=null){
                movePoints.add(new Vector3f(x, y+0.1f, z+1));
                z=z+1;
            }
            return;
        }
        if(suunta==3){
            if(roads[x][y][z-1]!=null){
                movePoints.add(new Vector3f(x, y+0.1f, z-1));
                z=z-1;
            }
            
        }
    }
    public void initXYZ(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
        roads=Main.roadMaker.roads;
    }
    
}
