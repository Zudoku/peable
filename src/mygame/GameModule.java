/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import mygame.GUI.IngameHUD;
import mygame.GUI.SelectionParticleEmitter;
import mygame.inputhandler.ClickingHandler;
import mygame.inputhandler.UserInput;
import mygame.shops.HolomodelDrawer;
import mygame.terrain.TerrainHandler;

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
    
  public GameModule(Node rootNode,AssetManager assetManager,AppSettings appSettings,Camera cam,InputManager input){
      this.rootNode=rootNode;
      this.assetManager=assetManager;
      this.appSettings=appSettings;
      this.camera=cam;
      this.input=input;
  }
    
  @Override 
  protected void configure() {

  }
  
  @Provides
  IngameHUD provideIngameHUD(){
      return new IngameHUD();
  }
  @Provides
  ClickingHandler provideClickingHandler(){
      return new ClickingHandler(null);
  }
  @Provides
  TerrainHandler provideTerrainHandler(Node node,AssetManager amanager){
      return new TerrainHandler(node, amanager);
  }
  @Provides
  Node provideRootNode(){
      return rootNode;
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
  LoadManager provideLoadManager(Node node,AppSettings settings,AssetManager amanager){
      return new LoadManager(node, settings, amanager);
  }
  @Provides
  HolomodelDrawer provideLoadManager(Node node,AssetManager amanager){
      return new HolomodelDrawer(amanager, node);
  }
  @Provides
  UserInput provideInput(Node node,InputManager inputm,Camera cam){
      return new UserInput(node,inputm,cam);
  }
  @Provides
  SelectionParticleEmitter provideInput(Node node,AssetManager amanager,TerrainHandler terrainh){
      return new SelectionParticleEmitter(amanager,node,terrainh);
  }
}