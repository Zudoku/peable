/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

import com.jme3.scene.Spatial;

/**
 *
 * @author arska
 */
public class BasicNPC {
    
    private String name;
    private Spatial object;
    public BasicNPC(String name,Spatial object){
        this.name=name;
        this.object=object;
        
    }
    public Spatial getGeometry(){
        return object;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        if(name==null){
            System.out.println("NAME IS NULL");
            return;
            
        }
     this.name=name;   
    }
    public void update(){
        
    }
    public void move(){
        
    }
    
    
    
}
