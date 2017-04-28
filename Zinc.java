package com.zinc.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

/*	TO-DO
* Animation (0/10)
* Finish collision detection (9/10)
* Show ray vectors for collision detection
* Organize classes in folders (0/10)
* Define my proportions, how many pixels in a foot (0/10)
* Debug menu (2/10)
* 
*/

/*	BUGS
 * Collision detection fails when screen moved
 * debug button is initially on "pressed" (fixed)
 * Collision detection slightly ghosting through
 * rayCastRight is doing halfway through sprite. It makes sense, idk how to fix atm though. Need to make it draw from right side
 * 
 */

public class Zinc extends ApplicationAdapter {

	private SceneLoader sceneLoader;
	private Viewport viewport;
	private Player player;
	
	private Stage uiStage;
	
	@Override
	public void create () {
		
		viewport = new FitViewport(426,240);
		sceneLoader = new SceneLoader();
		sceneLoader.loadScene("MainScene", viewport);
		
		ItemWrapper root = new ItemWrapper(sceneLoader.getRoot());
		
		player = new Player(sceneLoader.world);
		root.getChild("player").addScript(player);
		
		uiStage = new UIStage(sceneLoader.getRm());
		
		sceneLoader.addComponentsByTagName("platform", PlatformComponent.class);
		sceneLoader.getEngine().addSystem(new PlatformSystem());
		
		
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
		
		uiStage.act();
		uiStage.draw();//UI always after engine
		
		((OrthographicCamera)viewport.getCamera()).position.x = player.getX() + player.getWidth()/2f;//Sets x camera on player
		((OrthographicCamera)viewport.getCamera()).position.y = player.getY();//Sets y camera on player

	}
	
	@Override
	public void dispose () {

	}
}
