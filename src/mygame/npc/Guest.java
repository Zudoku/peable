/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.scene.Spatial;
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
    private GuestWalkingStates walkState= GuestWalkingStates.DONE;
    Spatial[][][]roads;
    
    
    public Guest(String name,float money,int guestNum,Spatial geom){
        super(name, geom);
       
        
        this.money=money;
        this.guestnum=guestNum;
                
    }

    @Override
    public void update() {
        //arvo suunta
        
        // liiku / osta /ole laitteessa /istu 
        
        //vähennä statseja jne esim nälkä jano tylsyys jne
    }
    public void randomizeStats(){
        
    }
    public void initXYZ(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
        roads=Main.roadMaker.roads;
    }
    
}
