/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.GUI;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import intopark.Main;
import intopark.UtilityMethods;
import intopark.inout.events.LoadFileEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arska
 */
public class StartScreen implements ScreenController {
    //LOGGER
    private static final Logger logger = Logger.getLogger(IngameHUD.class.getName());
    //DEPENDENCIES
    private Nifty nifty;
    private Screen screen;
    @Inject EventBus eventBus;
    //VARIABLES
    public int startgame=0;
    private File[] savedGames;
    private File selectedFile;
    private int timesClicked=2;
   

    public StartScreen(){
        
    }
    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);  // switch to another screen
    }

   public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }


    public void onStartScreen() {
        toggle();
        Main.injector.injectMembers(this);
    }
    public void toggle(){
        UtilityMethods.toggleVisibility(nifty,"newgamewindow");
        UtilityMethods.toggleVisibility(nifty,"loadgamewindow");
        populateSavedGames();
    }

    public void onEndScreen() {
       
    }
   
  
    public void startGametrue() {
        Main.startgame=1;
        startGame("hud");
        
        
    }
    public void startGameLoad(){
        eventBus.post(new LoadFileEvent(selectedFile));
        Main.startgame=5;
        startGame("hud");
    }
    public void populateSavedGames(){
        //Find the listbox
        ListBox loadgamelistbox=nifty.getCurrentScreen().findNiftyControl("loadgamelistbox", ListBox.class);
        //Find all the saves in the saves folder.
        File dir = new File("Saves");
    	savedGames= dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".IntoFile"); }
    	} );
        //Add them to the listbox
        for(File file:savedGames){
            loadgamelistbox.addItem(file.getName());
        }
    }

    /**
     * When the selection of the ListBox changes this method is called.
     */
    @NiftyEventSubscriber(id = "loadgamelistbox")
    public void onMyListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        
        File scenarioToBeLoaded=savedGames[event.getListBox().getFocusItemIndex()];
        if(scenarioToBeLoaded!=null){
            logger.log(Level.FINER,scenarioToBeLoaded.getName());
            if(selectedFile==null||selectedFile!=scenarioToBeLoaded){
                selectedFile=scenarioToBeLoaded;
                timesClicked=0;
            }else if (timesClicked==0){
                timesClicked++;
                logger.log(Level.FINEST,"{0}",timesClicked);
            }else{
                //LOAD THE FILE
                startGameLoad();
                //RESET SELECTED FILE FOR THE SAKE OF IT
                //selectedFile=null;
            }
            
        }
    }
    public void onLoadGameButton(){
        UtilityMethods.toggleVisibility(nifty,"loadgamewindow");
    }
    public void onLoadNewGameButton(){
        UtilityMethods.toggleVisibility(nifty,"newgamewindow");
    }
}
