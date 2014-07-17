/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import intopark.inout.Identifier;
import intopark.roads.RoadGraph;

/**
 *
 * @author arska
 */
public class GameModule extends AbstractModule {
    private final Node rootNode;
    private final AssetManager assetManager;
    private final AppSettings appSettings;
    private final Camera camera;
    private final InputManager input;
    private final EventBus eventBus=new EventBus();
    private final Identifier identifier;
    private RoadGraph roadGraph=new RoadGraph();

  public GameModule(Node rootNode,AssetManager assetManager,AppSettings appSettings,Camera cam,InputManager input){
      this.rootNode=rootNode;
      this.assetManager=assetManager;
      this.appSettings=appSettings;
      this.camera=cam;
      this.input=input;
      this.identifier=new Identifier();
  }

  @Override
  protected void configure() {

  }

  @Provides
  Identifier provideIdentifier(){
      return identifier;
  }

  @Provides
  Node provideRootNode(){
      return rootNode;
  }
  @Provides
  RoadGraph provideroadGraph(){
      roadGraph.setEventBus(eventBus);
      return roadGraph;
  }
  @Provides
  Camera provideCamera(){
      return camera;
  }
  @Provides
  AssetManager provideAssetManager(){
      return assetManager;
  }
  @Provides
  AppSettings provideAppSettings(){
      return appSettings;
  }
  @Provides
  InputManager provideInputManager(){
      return input;
  }
  @Provides
  EventBus provideEventBus(){
      return eventBus;
  }




}