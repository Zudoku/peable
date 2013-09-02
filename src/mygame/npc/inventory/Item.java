/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.npc.inventory;

/**
 *
 * @author arska
 */
public class Item {
    String name;
    Itemtypes itemtype;
    public Item(String name,Itemtypes itemtype){
        this.name=name;
        this.itemtype=itemtype;
    }
}
