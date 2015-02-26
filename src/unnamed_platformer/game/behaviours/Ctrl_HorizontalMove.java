package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.physics.PhysicsInstance;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;

public class Ctrl_HorizontalMove extends ControlMechanism {

	private float acceleration, deceleration, maxSpeed;

	public float getDeceleration() {
		return deceleration;
	}

	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float accel) {
		this.acceleration = accel;
	}

	public Ctrl_HorizontalMove(ActiveEntity actor, float acceleration,
			float deceleration, float maxSpeed) {
		super(actor);
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public void doUpdate(float multiplier) {
		PhysicsInstance physics = actor.getPhysics();
		Vector2f velocity = physics.getVelocity();

		float effectiveAccel = acceleration * multiplier;
		float effectiveDecel = deceleration * multiplier;
		float effectiveMaxSpeed = maxSpeed * multiplier;

		float horizontalSpeed = Math.abs(velocity.x);
		boolean lessThanMaxSpeed = horizontalSpeed < effectiveMaxSpeed;
		boolean movingLeft = velocity.x < 0;
		boolean notMoving = velocity.x == 0;

		boolean rightPressed = InputManager.keyIsPressed(GameKey.RIGHT, 1);
		boolean leftPressed = InputManager.keyIsPressed(GameKey.LEFT, 1);

		Vector2f baseForce = new Vector2f(0, 0);

		if (rightPressed) {
			if (!movingLeft || notMoving) {
				if (lessThanMaxSpeed) {
					baseForce.x = effectiveAccel;
				}
			} else {
				baseForce.x = effectiveDecel;
			}
		}

		if (leftPressed) {
			if (movingLeft || notMoving) {
				if (lessThanMaxSpeed) {
					baseForce.x = -effectiveAccel;
				}
			} else {
				baseForce.x = -effectiveDecel;
			}
		}

		// add drag when no key pressed
		if (!leftPressed && !rightPressed && !notMoving) {

			// don't start moving the other direction!
			float maxDecel = effectiveDecel < horizontalSpeed ? effectiveDecel
					: horizontalSpeed;

			if (movingLeft) {
				baseForce.x = maxDecel;
			} else {
				baseForce.x = -maxDecel;
			}
		}

		
		physics.addForce(baseForce);
	}

	public void reset() {

	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
