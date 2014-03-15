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
    BasicShop b;
    public ShopDemolishEvent(BasicShop b){
        this.b=b;
    }
    public BasicShop getShop(){
        return b;
    }
}
