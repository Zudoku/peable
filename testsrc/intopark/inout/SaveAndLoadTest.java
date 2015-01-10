/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.google.common.eventbus.EventBus;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import intopark.UtilityMethods;
import intopark.npc.Guest;
import intopark.npc.NPCManager;
import intopark.npc.events.CreateGuestEvent;
import intopark.npc.inventory.Item;
import intopark.npc.inventory.Itemtypes;
import intopark.npc.inventory.StatManager;
import intopark.npc.inventory.Wallet;
import intopark.ride.RideColor;
import intopark.roads.Road;
import intopark.roads.RoadHill;
import intopark.roads.RoadManager;
import intopark.roads.RoadGraph;
import intopark.roads.events.CreateRoadEvent;
import intopark.shops.BasicShop;
import intopark.shops.CreateShopEvent;
import intopark.shops.ShopManager;
import intopark.shops.ShopReputation;
import intopark.terrain.MapContainer;
import intopark.terrain.ParkHandler;
import intopark.util.Direction;
import intopark.util.MapPosition;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author arska
 */
public class SaveAndLoadTest {

    public SaveAndLoadTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveAndLoad() {
        //SET UP
        EventBus bus=new EventBus();
        //ASSETMANAGER
        AssetManager asmang=Mockito.mock(AssetManager.class);
        Mockito.stub(asmang.loadModel(LoadPaths.guest)).toReturn(new Node("Guest"));
        Mockito.stub(asmang.loadModel(LoadPaths.toilet)).toReturn(new Node("toilet"));
        UtilityMethods um=new UtilityMethods(null, null, asmang, null,null);

        //PARKHANDLER
        Node node=new Node("");
        ParkHandler ph=new ParkHandler(node, null, null,bus);
        ph.setIdentifier(new Identifier());
        //ROADMAKER
        RoadManager rm=Mockito.mock(RoadManager.class);
        Mockito.stub(rm.getRoadGraph()).toReturn(new RoadGraph());
        ph.setRoadMaker(rm);
        //NPCMANAGER
        NPCManager npcm=new NPCManager(new Node(""),null,bus,null);
        ph.setNpcManager(npcm);
        //SHOPMANAGER
        ShopManager sm=new ShopManager(new Node(""), null, bus,null);
        ph.setShopManager(sm);
        ph.setMap(new MapContainer(bus));

        bus.register(ph);
        //CREATE A GUEST
        Wallet gwallet=new Wallet(100.0f);
        StatManager stats=new StatManager();
        stats.randomize();
        List<Item> items=new ArrayList<>();
        items.add(new Item("funitem", Itemtypes.FUN,10));
        CreateGuestEvent event1= new CreateGuestEvent(gwallet,items, 20, Direction.NORTH,10, 10, 10,stats ,new Node("a"),"test name",ph,true,1, RideColor.GRAY);
        //CREATE A SHOP
        CreateShopEvent event2= new CreateShopEvent("toilet","shopname","prodname", ShopReputation.BAD,10,100,30,new MapPosition(10, 10, 10), Direction.NORTH,bus);
        //CREATE 2 ROADS
        CreateRoadEvent event3= new CreateRoadEvent(new Road(new MapPosition(8, 8, 8), RoadHill.FLAT, 9, 1,false, Direction.NORTH));
        CreateRoadEvent event4= new CreateRoadEvent(new Road(new MapPosition(9, 8, 9), RoadHill.UP, 11, 1,true, Direction.EAST));

        bus.post(event1);
        bus.post(event2);
        bus.post(event3);
        bus.post(event4);
        //SAVE
        SaveManager instance = new SaveManager(ph);
        instance.Save("testing_save");
        bus.unregister(ph);
        //LOAD
        bus=new EventBus();
        ParkHandler ph2=new ParkHandler(new Node(""), null, null, bus);
        ph2.setIdentifier(new Identifier());
        Node node2=new Node("");
        //NPCMANAGER
        npcm=new NPCManager(new Node(""),null,bus,null);
        ph2.setNpcManager(npcm);
        //SHOPMANAGER
        sm=new ShopManager(new Node(""), null, bus,null);
        ph2.setShopManager(sm);
        ph2.setMap(new MapContainer(bus));

        //bus.register(ph2);
        LoadManager instance2 =new LoadManager(node2,ph2, bus);

        instance2.load("Saves/testing_save.IntoFile");

        //TEST IF GUEST-DATA IS SAVED
        Guest g1=ph.getGuests().get(0);//IF TEST DIES HERE, IT MEANS THAT IT WASNT SAVED AT ALL
        Guest g2=ph2.getGuests().get(0);//IF TEST DIES HERE, IT MEANS THAT IT WASNT SAVED AT ALL

        assertEquals(g1.getID(), g1.getID());
        assertEquals(g1.getMoving(), g2.getMoving());
        assertEquals(g1.getName(), g2.getName());

        assertEquals(g1.getStats().getHappyness(), g2.getStats().getHappyness());
        assertEquals(g1.getStats().getHunger(), g2.getStats().getHunger());
        assertEquals(g1.getStats().getPreferredRide(), g2.getStats().getPreferredRide());
        assertEquals(g1.getStats().getThirst(), g2.getStats().getThirst());
        assertEquals(g1.getWallet().getmoney(), g2.getWallet().getmoney(),1);

        assertEquals(g1.getPosition().getX(), g2.getPosition().getX());
        assertEquals(g1.getPosition().getY(), g2.getPosition().getY());
        assertEquals(g1.getPosition().getZ(), g2.getPosition().getZ());

        Item item1=g1.getInventory().get(0);
        Item item2=g2.getInventory().get(0);
        assertEquals(item1.getConsumevalue(), item2.getConsumevalue());
        assertEquals(item1.getDurability(), item2.getDurability());
        assertEquals(item1.getItemtype(), item2.getItemtype());
        assertEquals(item1.getName(), item2.getName());

        assertEquals(g1.isMale(), g2.isMale());
        assertEquals(g1.getColor(), g2.getColor());
        assertEquals(g1.getSize(), g2.getSize());

        //TEST IF SHOP-DATA IS SAVED
        BasicShop s1=ph.getShops().get(0); //IF TEST DIES HERE, IT MEANS THAT IT WASNT SAVED AT ALL
        BasicShop s2=ph2.getShops().get(0); //IF TEST DIES HERE, IT MEANS THAT IT WASNT SAVED AT ALL

        assertEquals(s1.getPosition().getX(), s2.getPosition().getX());
        assertEquals(s1.getPosition().getY(), s2.getPosition().getY());
        assertEquals(s1.getPosition().getZ(), s2.getPosition().getZ());

        assertEquals(s1.getConstructionmoney(), s2.getConstructionmoney(),1);
        assertEquals(s1.getDirection(), s2.getDirection());
        assertEquals(s1.getPrice(), s2.getPrice(),1);
        assertEquals(s1.getProductname(), s2.getProductname());
        assertEquals(s1.getID(), s2.getID());
        assertEquals(s1.getShopName(), s2.getShopName());
        assertEquals(s1.getType(), s2.getType());

        //TEST IF ROAD-DATA IS SAVED
        //TODO:
    }
}