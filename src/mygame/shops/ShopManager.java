/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shops;

import java.util.ArrayList;

/**
 *
 * @author arska
 */
public class ShopManager {
    
    BasicShop selectedShop;
    ArrayList<BasicShop> shops=new ArrayList<BasicShop>();
    
    public ShopManager(){
        
        
    }
    
    public void buy(){
        
        
    }
    public void setSelection(BasicShop select){
        if(select==null){
            return;
        }
        this.selectedShop=select;
    }
    
    
    
}
