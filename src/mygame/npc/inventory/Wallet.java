/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc.inventory;

/**
 *
 * @author arska
 */
public class Wallet {

    private float money;
    public Wallet(Float money) {
        this.money=money;
    }
    public void pay(float pay){
        money=money-pay;
    }
    public boolean canAfford(float pay){
        if((money-pay)>0){
            return true;
        }
        return false;
    }
    public float getmoney(){
       return money;
    }
}