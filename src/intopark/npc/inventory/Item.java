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
    private String name;
    private Itemtypes itemtype;
    private int consumevalue;
    private int durability=10;
    public Item(String name,Itemtypes itemtype,int consumevalue){
        this.name=name;
        this.itemtype=itemtype;
        this.consumevalue=consumevalue;
    }
    public void consume(StatManager parentstats){
        switch(itemtype){
            case DRINK:
                parentstats.setThirst(parentstats.getThirst()-consumevalue);
                
                break;
                
            case FOOD:
                parentstats.setHunger(parentstats.getHunger()-consumevalue);
                break;
                
            case FUN:
                parentstats.setHappyness(parentstats.getHappyness()+consumevalue);
                break;
        }
    }
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
     
    public int getConsumevalue() {
        return consumevalue;
    }

    public int getDurability() {
        return durability;
    }

    public Itemtypes getItemtype() {
        return itemtype;
    }

    public void setConsumevalue(int consumevalue) {
        this.consumevalue = consumevalue;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void setItemtype(Itemtypes itemtype) {
        this.itemtype = itemtype;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
