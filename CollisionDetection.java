package com.zinc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

public class CollisionDetection {
	
	Player player = new Player();
	TransformComponent transformComponent;
	DimensionsComponent dimensionsComponent;
	Vector2 speed;
	World world;
	
	public CollisionDetection(Player player){ //idk about this whole constructor, kinda want it gone
		this.transformComponent = player.getTransformComponent();
		this.dimensionsComponent = player.getDimensionsComponent();
		this.speed = player.getSpeed();
		this.world = player.getWorld();
		
	}
	
	//public Player rayCastDown(DimensionsComponent dimensionsComponent, TransformComponent transformComponent, Vector2 speed, World world) {
	public void rayCastDown(Player player) {
		this.player = player;
		this.transformComponent = player.getTransformComponent();
		this.dimensionsComponent = player.getDimensionsComponent();
		this.speed = player.getSpeed();
		this.world = player.getWorld();
		float rayGap = dimensionsComponent.height/2;
		
		//Ray size is the exact size of the deltaY change we plan for this frame
		float raySize = -(speed.y)*Gdx.graphics.getDeltaTime(); //old/unused (speed.y + Gdx.graphics.getDeltaTime())
		
		if(raySize < 3f) raySize = 3f; //checks if ray is too small
		
		// only check for collisions when moving down
		if(speed.y > 0) return;
		
		//Vectors of ray from middle middle
		Vector2 rayFrom = new Vector2((transformComponent.x + dimensionsComponent.width/2)*PhysicsBodyLoader.getScale(), (transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2((transformComponent.x + dimensionsComponent.width/2)*PhysicsBodyLoader.getScale(), (transformComponent.y - raySize) * PhysicsBodyLoader.getScale());
		
		//Cast the ray
		world.rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				//stop the player
				player.setSpeedY(0);
				//speed.y = 0;
				
				
				//reposition player slightly upper the collision point
				player.setTransformComponentY(point.y / PhysicsBodyLoader.getScale() + 0.01f);
				//transformComponent.y = point.y / PhysicsBodyLoader.getScale() + 0.01f;
				
				return 0;
			}
		}, rayFrom, rayTo);
		
		//Debug, projects the rayCast vector
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.translate(640, 360, 0);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.line(rayFrom.x, rayFrom.y, rayTo.x, rayTo.y * -10f);
		shapeRenderer.end();
		return player;
		
	}
}
