/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;


/**
 *
 * @author arska
 */
public class Button {
    private String name;
    private final String ref;
    public final GUIListener listener;
    private float x;
    private float y;
    private final int width;
    private final int height;
    Picture img;
    private final Node guiNode;
    ArrayList<Button> buttons=new ArrayList();
    

    
    public Button(String name,String ref,GUIListener listener,int x,int y,int width,int height,AssetManager assetManager,Node guiNode,ArrayList<Button> list){
        this.name=name;
        this.ref=ref;
        this.listener=listener;
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.guiNode=guiNode;
        this.buttons=list;
        img=new Picture(name);
        img.setImage(assetManager, ref, true);
        img.setHeight(height);
        img.setWidth(width);
        img.setPosition(x, y);
        buttons.add(this);
        guiNode.attachChild(img);
    }
    
    public String getName() {
        return name;
    }
    
    public boolean testHit(float mx,float my){
        if(x+width-mx>0&&x+width-mx<width){
            if(y+height-my>0&&y+height-my<height){
                return true;
            }
        }
        return false;
    }
    public void move(float movex,float movey){
        x=x+movex;
        y=y+movey;
    }
    public void setLocation(float x,float y){
        this.x=x;
        this.y=y;
    }

    
}
