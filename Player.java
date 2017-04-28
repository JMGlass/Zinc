package com.zinc.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class Player implements IScript{
	
	private Entity player;
	private TransformComponent transformComponent;
	private DimensionsComponent dimensionsComponent;
	private World world;
	private CollisionDetection collisionDetection;
	
	private Vector2 speed;
	private float gravity = -200f; //200
	
	private final float jumpSpeed = 300f;
	
	public Player(){
	}	
	
	public Player(World world){
		this.world = world;
	}	

	@Override
	public void init(Entity entity) {
		player = entity;
		
		transformComponent = ComponentRetriever.get(entity, TransformComponent.class); //transform sprite
		dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class); //dimensions of sprite
		
		speed = new Vector2(100, 0);
	}

	@Override
	public void act(float delta) {
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			transformComponent.x -= speed.x * delta * 2;
			transformComponent.scaleX = -1f;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			transformComponent.x += speed.x * delta * 2;
			transformComponent.scaleX = 1f;
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			speed.y = jumpSpeed;
		}
		
		speed.y += gravity * delta * 2.7;
		
		transformComponent.y += speed.y * delta;
		
		
		transformComponent.y = collisionDetection.rayCastDown(dimensionsComponent, transformComponent, speed, world).getTransformComponentY();
		rayCastUp();
		rayCastLeft();
		rayCastRight();
	}
	
	private void rayCastDown() {

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
				speed.y = 0;
				
				//reposition player slightly upper the collision point
				transformComponent.y = point.y / PhysicsBodyLoader.getScale() + 0.01f;
				
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
	}

	private void rayCastUp() {

		float rayGap = dimensionsComponent.height/2;
		
		//Ray size is the exact size of the deltaY change we plan for this frame
		float raySize = (speed.y)*Gdx.graphics.getDeltaTime();
		
		if(raySize < 3f) raySize = 3f; //checks if ray is too small
		
		// only check for collisions when moving down
		if(-speed.y > 0) return;
		
		//Vectors of ray from middle middle
		Vector2 rayFrom = new Vector2((transformComponent.x + dimensionsComponent.width/2)*PhysicsBodyLoader.getScale(),  (transformComponent.y - rayGap) * PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2((transformComponent.x + dimensionsComponent.width/2)*PhysicsBodyLoader.getScale(), ((transformComponent.y + raySize) * PhysicsBodyLoader.getScale()) + 0.3f);

		//Cast the ray
		world.rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				//stop the player
				speed.y = 0;
				
				//reposition player slightly lower of the collision point
				transformComponent.y = point.y / PhysicsBodyLoader.getScale() - 11f; //-11f is top of the top
				
				return 0;
				
			}
		}, rayFrom, rayTo);
		
		//Debug, projects the rayCast vector
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.translate(640, 360, 0);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.line(rayFrom.x, rayFrom.y, rayTo.x, rayTo.y * 10f);
		shapeRenderer.end();
	}
	
	private void rayCastLeft() {

		float rayGap = dimensionsComponent.width/2;
		
		//Ray size is the exact size of the deltaY change we plan for this frame
		float raySize = -(speed.x)*Gdx.graphics.getDeltaTime(); //old/unused (speed.y + Gdx.graphics.getDeltaTime())
		
		if(raySize < 3f) raySize = 3f; //checks if ray is too small
		
		// only check for collisions when moving
		if(speed.x == 0) return;
		
		//Vectors of ray from middle middle
		Vector2 rayFrom = new Vector2((transformComponent.x + rayGap) * PhysicsBodyLoader.getScale(), (transformComponent.y + dimensionsComponent.height/2) * PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2((transformComponent.x - raySize) * PhysicsBodyLoader.getScale(), (transformComponent.y + dimensionsComponent.height/2) * PhysicsBodyLoader.getScale());

		//Cast the ray
		world.rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				//stop the player
				speed.x = 0;
				
				//reposition player slightly right of the collision point
				transformComponent.x = point.x / PhysicsBodyLoader.getScale() + 0.01f;
				speed.x = 100;
				return 0;
				
			}
		}, rayFrom, rayTo);
		
		//Debug, projects the rayCast vector
		rayTo.x = rayTo.x *10f;
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.translate(640, 360, 0);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.line(rayFrom.x, rayFrom.y, rayTo.x, rayTo.y);
		shapeRenderer.end();
	}
	
	private void rayCastRight() {

		float rayGap = dimensionsComponent.width/2;
		
		//Ray size is the exact size of the deltaY change we plan for this frame
		float raySize = -(speed.x)*Gdx.graphics.getDeltaTime(); //old/unused (speed.y + Gdx.graphics.getDeltaTime())
		
		//if(raySize < 3f) raySize = 3f; //checks if ray is too small
		
		// only check for collisions when moving
		if(speed.x == 0) return;
		
		//Vectors of ray from middle middle
		Vector2 rayFrom = new Vector2((transformComponent.x - rayGap) * PhysicsBodyLoader.getScale(), (transformComponent.y + dimensionsComponent.height/2) * PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2((transformComponent.x + raySize) * PhysicsBodyLoader.getScale(), (transformComponent.y + dimensionsComponent.height/2) * PhysicsBodyLoader.getScale());

		//Cast the ray
		world.rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				//stop the player
				speed.x = 0;
				
				//reposition player slightly left of the collision point
				transformComponent.x = point.x / PhysicsBodyLoader.getScale() - 1f;
				speed.x = 100;
				return 0;
				
			}
		}, rayFrom, rayTo);
	}
	
	public float getX(){
		return transformComponent.x;
	}
	
	public float getY(){
		return transformComponent.y;
	}
	
	public void setSpeedY(float speedY){
		speed.y = speedY;
	}

	public void setSpeedX(float speedX){
		speed.x = speedX;
	}
	
	public TransformComponent getTransformComponent(){
		return transformComponent;
	}
	
	public DimensionsComponent getDimensionsComponent(){
		return dimensionsComponent;
	}
	
	public Vector2 getSpeed(){
		return speed;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public float getTransformComponentX(){
		return transformComponent.x;
	}
	
	public void setTransformComponentY(float transformComponentY){
		transformComponent.y = transformComponentY;
	}
	
	public void setTransformComponentX(float transformComponentX){
		transformComponent.x = transformComponentX;
	}
	
	public float getWidth() {
		return dimensionsComponent.width;
	}
	
	@Override
	public void dispose() {
	}
}
