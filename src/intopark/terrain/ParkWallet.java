/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.terrain;

/**
 *
 * @author arska
 */
public class ParkWallet {
    float money=10000;
    float loan=0;
    public ParkWallet(float money){
        this.money=money;
        //TODO: SHOULD EXTEND NORMAL WALLET.
    }
    public void add(float add){
        money += add;
    }
    public void remove(float cost){
       money -= cost; 
    }
    public boolean canAfford(float cost){
        if(money-cost>0){
            return true;
        }
        return false;
    }
    public float getMoney(){
        return money;
    }
    public float getLoan(){
        return loan;
    }

    public String getMoneyString() {
        return Float.toString(money);
    }
    public String getLoanString() {
        return Float.toString(loan);
    }
    public void setLoan(Float loan){
        this.loan=loan;
    }
    
}
