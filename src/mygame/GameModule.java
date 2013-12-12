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
  
  
  
  
}