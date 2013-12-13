/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.terrain;

/**
 *
 * @author arska
 */
public class PayParkEvent {
    private final Float amount;
    public PayParkEvent(Float amount){
        this.amount=amount;
    }
    public float getAmount(){
        return amount;
    }
}
