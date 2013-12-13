/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

/**
 *
 * @author arska
 */
public class ShopDemolishEvent {
    BasicShop b;
    public ShopDemolishEvent(BasicShop b){
        this.b=b;
    }
    public BasicShop getShop(){
        return b;
    }
}
