/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

/**
 *
 * @author arska
 */
public class ParkWallet {
    float money=10000;
    float loan=0;
    public ParkWallet(float money){
        this.money=money;
    }
    public void add(float add){
        money += add;
    }
    public void remove(float cost){
       money -= cost; 
    }
    public boolean canAfford(float cost){
        if(money-cost>0){
            return true;
        }
        return false;
    }
    public float getMoney(){
        return money;
    }
    
}
