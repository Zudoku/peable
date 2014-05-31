/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.inject.Singleton;
import intopark.shops.BasicBuildables;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arska
 */
@Singleton
public class ShopDescriptionManager {
    private Map<BasicBuildables,String[]>values=new HashMap<>();
    private String currentName="Name:";
    private String currentDescription_1="Description: ";
    private String currentDescription_2="";
    private String currentDescription_3="";
    private String currentDescription_4="";
    private String currentPrice="Price: ";
    private String currentBigPic="Interface/Shops/Icon.png";
    
    public ShopDescriptionManager(){
        values.put(BasicBuildables.ARCHERYRANGE,new String[]{"Name: Archery Range","Description: Archery range is an area where aaaaa"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.BALLOON_STAND,new String[]{"Name: Balloon Stand","Description: Kids love these things! Try placing these around every corner and see for yourself. Balloons come in every color and shape imaginable."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.BLENDER,new String[]{"Name: Blender","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.CAFE,new String[]{"Name: Cafe","Description: Cheap looking Coffee van that makes ","the famous The MR. brand coffee. Coffee-machine","looks very expensive but it's unfortunately very dirty."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.CANDY_STREET,new String[]{"Name: Candy Street","Description: This shop serves a long aisle of candy.","Candy is very expensive but very high quality."," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.CANTHROW,new String[]{"Name: Can Throw","Description: Have you ever wanted to just throw something? Let your guests have some fun by throwing balls at can formations. If they get the whole thing down, they get a teddybear!"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.CHESSCENTER,new String[]{"Name: ChessCenter","Description: "," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.CHICKEM,new String[]{"Name: Chick'EM","Description: Serves fried chicken in every shape","and form. If your looking for something that is able","to feed many people for cheap, this is your best bet."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.CHIPS_N_CHICKEN,new String[]{"Name: Chips N Chicken","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.ENERGY,new String[]{"Name: Energy Shop","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.FAST_JOY,new String[]{"Name: Fast Joy","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.HAUNTEDHOUSE,new String[]{"Name: HauntedHouse","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.INFOBOARD,new String[]{"Name: Info Board","Description: Big board with tons of information for the guests. Includes a parkmap but no staff."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.INFO_KIOSK,new String[]{"Name: Info Kiosk","Description: A small building that serves as a help center for visitors. Full on customer support and other luxuries. It's good to have a few of these around."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.INTOS_ICECREAM,new String[]{"Name: Into's Icecream","Description: Ice cream parlor .You can get almost","everything from there: soft serve, ice cream and","ice lolly. Guess where it got its name.  "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.JOKE_VEND,new String[]{"Name: Joke Vend","Description: Vending machine that sells little silly pranking items and junk. Its a gamble if the customers will like this or not."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.MEATBALLS,new String[]{"Name: Meatballs","Description: Traditional Swedish meatballs cooked","right below your own eyes. One of the few real eating","places that serve good food but it surely pays its","price","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.MILKSHJAKE,new String[]{"Name: Milkshjake","Description: Milkshakes for everyone! Every flavour","imaginable. One of the classic must have shops","as the customers love this place."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.NAKK_KIOSK,new String[]{"Name: Nakk Kiosk","Description: Kiosk that sells hamburgers , hotdogs ","and the like. Customers can choose the extra","toppings."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.NEWS_STAND,new String[]{"Name: News Stand","Description: Edjucate your customers and letting them buy news paper and books from this compact but great stand. Also sells maps and comics."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.NORMAI_FOOD,new String[]{"Name: Norma'I Food","Description: A restaurant that serves the most exotic","and the most strangest foods on the planet.","WARNING: If not operated properly, might kill","customers.","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.NULL,new String[]{"",""," "," "," ","","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.PASTRY_SHOP,new String[]{"Name: Pastry Shop","Description: Experience the pastries straight from","the local bakery. Optimal placement for this shop","should be near the enterance since its handy to buy","from it as you leave.","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.PICK_ROPE,new String[]{"Name: Pick Rope","Description: Rope pulling stand. This is great for trying to please the customers as everybody wins something!"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.PIRATESHIP,new String[]{"Name: PirateShip","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.POPCORNVAN,new String[]{"Name: Popcorn Van","Description: Greasy popcorn cooked in cart looking","kiosk. Very popular but negatively harms your","customers health."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.ROTOR,new String[]{"Name: Rotor","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.SODAMACHINE,new String[]{"Name: Sodamachine","Description: Customer friendly soda vending","machine. Insert coins and get a drink. Must be filled","and fixed regularly."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.SPINWHEEL,new String[]{"Name: Spinwheel","Description: TODO:"," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.SWEETCORN,new String[]{"Name: SweetCorn","Description: Sells normal,grilled and mashed","sweetcorn. Its a little weird food."," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.SWEETFRUITS,new String[]{"Name: Sweetfruits","Description: Serves fruit cups."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.TOILET,new String[]{"Name: Toilet","Description: Does this building need any explaining? Its good to have plenty of these. Remember to fix and clean them properly since that heavily affects your parks reputation."," "," "," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.TRACHFOOD,new String[]{"Name: Trachfood","Description: Sells the absolute nastiest foods ever.","It's extremely cheap but negatively affects your","parks reputation."," ","Price: 999$","Interface/Shops/Icon.png"});
        values.put(BasicBuildables.WRAP_PLACE,new String[]{"Name: Wrap Place","Description: Perfect homemade meals for anyone","looking for an easy snack on the go. It's good to","experiment with different kinds of flavours."," ","Price: 999$","Interface/Shops/Icon.png"});
    }
    public void setDescription(BasicBuildables buildable) {
        String[]descriptionContainer=values.get(buildable);
        if(descriptionContainer!=null){
            currentName=descriptionContainer[0];
            currentDescription_1=descriptionContainer[1];
            currentDescription_2=descriptionContainer[2];
            currentDescription_3=descriptionContainer[3];
            currentDescription_4=descriptionContainer[4];
            currentPrice=descriptionContainer[5];
            currentBigPic=descriptionContainer[6];
        }else{
            currentName="CONF";
            currentDescription_1="CONf";
            currentDescription_2=" ";
            currentDescription_3=" ";
            currentDescription_4=" ";
            currentPrice="CONF";
            currentBigPic="Interface/Shops/Icon.png";
            
        }
    }

    public String getCurrentBigPic() {
        return currentBigPic;
    }

    public String getCurrentDescription_1() {
        return currentDescription_1;
    }

    public String getCurrentDescription_2() {
        return currentDescription_2;
    }

    public String getCurrentDescription_3() {
        return currentDescription_3;
    }

    public String getCurrentDescription_4() {
        return currentDescription_4;
    }
    

    public String getCurrentName() {
        return currentName;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }
    

    
    
}
