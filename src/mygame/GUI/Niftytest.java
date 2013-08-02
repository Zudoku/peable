/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;

/**
 *
 * @author arska
 */
public class Niftytest {

    public Niftytest(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort) {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        // <screen>
        nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen") {
            {
                controller(new DefaultScreenController()); // Screen properties       

                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        // <panel>
                        panel(new PanelBuilder("Panel_ID") {
                            {
                                childLayoutCenter(); // panel properties, add more...               

                                // GUI elements
                                control(new ButtonBuilder("Button_ID", "Hello Nifty") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        height("5%");
                                        width("15%");
                                    }
                                });

                                //.. add more GUI elements here              

                            }
                        });
                        // </panel>
                    }
                });
                // </layer>
            }
        }.build(nifty));
        // </screen>

        nifty.gotoScreen("Screen_ID"); // start the screen
    }
}
