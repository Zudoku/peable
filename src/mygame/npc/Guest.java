/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc;

/**
 *
 * @author arska
 */
public class Guest extends BasicNPC{
    
    private float money=10;
    
    
    public Guest(String name,float money){
        super(name, null);
        this.money=money;
                
    }

    @Override
    public void update() {
        //arvo suunta
        
        // liiku / osta /ole laitteessa /istu 
        
        //vähennä statseja jne esim nälkä jano tylsyys jne
    }
    
}
