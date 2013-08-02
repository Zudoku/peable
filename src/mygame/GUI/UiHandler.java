/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import java.util.ArrayList;

/**
 *
 * @author arska
 */
public class UiHandler {
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final Node guiNode;
    private GUIListener guiListener;
    ArrayList<Button> buttons;
    private final InputManager iNputManager;
    

    public UiHandler(AppSettings settings, AssetManager assetManager, Node guiNode,InputManager inputManager) {
        this.settings=settings;
        this.assetManager=assetManager;
        this.guiNode=guiNode;
        this.iNputManager=inputManager;
        buttons=new ArrayList<Button>();
        guiListener=new GUIListener() {

            public void oAction(String name) {
                if(name=="Shovel"){
                    //avaa shovel
                }
                if(name=="Roads"){
                    //avaa roads
                }
                
            }
        };
    }
    public void makeButtons() {
        Button Terrain=new Button("Shovel", "Interface/Terrain/Icon.png",guiListener,settings.getWidth()-64,settings.getHeight()-64, 64, 64, assetManager, guiNode,buttons);
        Button Road=new Button("Roads", "Interface/Roads/Icon.png",guiListener,settings.getWidth()-128,settings.getHeight()-64, 64, 64, assetManager, guiNode,buttons);
    }
    
    public ArrayList<Button> getButtonList(){
        return buttons;
    }

}
