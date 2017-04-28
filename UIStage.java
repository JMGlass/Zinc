package com.zinc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;

public class UIStage extends Stage {
	
	public UIStage(IResourceRetriever ir){
		
		Gdx.input.setInputProcessor(this);
		
		ProjectInfoVO projectInfo = ir.getProjectVO();
		
		//Creates object of the button (i can form all ui buttons into one UI library item if i wanted)
		CompositeItemVO debugButtonData = projectInfo.libraryItems.get("debugButton");//pulls debugButton from library in overlap2D
		CompositeActor buttonActor = new CompositeActor(debugButtonData, ir);//CompositeActor has all the method for retrieving items by tag names or w/e
		addActor(buttonActor);//Adds actor to the root of the stage
		//first method in tut 2 for static activators(buttons)
		
		//buttonActor.setX(getWidth() - buttonActor.getWidth()); //Sets the button at the right
		buttonActor.setY(getHeight() - buttonActor.getHeight()); //Sets the button at the top
		
		//The click listener for the button
		buttonActor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("debug");
			}
		});
	}
}
