package model.logic;

import model.Ref.Flag;
import model.entities.ActiveEntity;
import model.entities.Entity;

import org.lwjgl.util.vector.Vector2f;

public class PhysicsProcessor {

	public static Vector2f gravity = new Vector2f(0, 4f);
	public static float FORCE_SCALE = 0.15f;

	public static Vector2f calculateGravity(double airTime) {
		return new Vector2f(0,
				(float) (gravity.getY() * Math.pow(airTime, 0.5)));
	}

	public static void doSomethingNew(Entity actor, float delta) {

	}

	public static void applyGravity(ActiveEntity actor, float delta) {
		if (actor.checkFlag(Flag.obeysGravity)) {

			actor.physics.addForce(PhysicsProcessor
					.calculateGravity(actor.physics.airTime));
			actor.physics.inAir = true;
			actor.physics.airTime += delta / 1000.0;
		}
	}

}
