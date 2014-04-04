/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.shops;

import com.google.inject.Singleton;

/**
 *
 * @author arska
 */
@Singleton
public class ShopDemolishEvent {
    private BasicShop b;
    /**
     * Event used to demolish(DESTROY) shop building.
     * @param b shop to demolish
     */
    public ShopDemolishEvent(BasicShop b){
        this.b=b;
    }
    public BasicShop getShop(){
        return b;
    }
}
