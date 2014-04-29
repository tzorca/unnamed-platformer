package model.logic;

import org.lwjgl.util.vector.Vector2f;

public class PhysicsProcessor {

	public static Vector2f gravity = new Vector2f(0, 4f);
	public static float FORCE_SCALE = 0.01f;

	public static Vector2f calculateGravity(double airTime) {
		return new Vector2f(0,
				(float) (gravity.getY() * Math.pow(airTime, 0.5)));
	}

}
