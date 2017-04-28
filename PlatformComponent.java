package com.zinc.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.ashley.core.Component;

public class PlatformComponent implements Component{

	Vector2 originalPosition;
	int timePassed = 0;
}
