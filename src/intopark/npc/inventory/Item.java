/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.npc.inventory;

/**
 *
 * @author arska
 */
public class Item {
    String name;
    Itemtypes itemtype;
    int consumevalue;
    int durability=10;
    public Item(String name,Itemtypes itemtype,int consumevalue){
        this.name=name;
        this.itemtype=itemtype;
        this.consumevalue=consumevalue;
    }
    public void consume(StatManager parentstats){
        switch(itemtype){
            case DRINK:
                parentstats.thirst=parentstats.thirst-consumevalue;
                
                break;
                
            case FOOD:
                parentstats.hunger=parentstats.hunger-consumevalue;
                break;
                
            case FUN:
                parentstats.happyness=parentstats.happyness+consumevalue;
                break;
        }
    }
    @Override
    public String toString() {
        return name;
    }
    
}
