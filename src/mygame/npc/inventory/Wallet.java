/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc.inventory;

import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class Wallet {
    private  transient static final Logger logger = Logger.getLogger(Wallet.class.getName());
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
    @Override
    public String toString() {
        return Float.toString(money);
    }
}
