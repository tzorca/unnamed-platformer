package game.dynamics.control_mechanisms;

import game.PhysicsInstance;
import game.entities.ActiveEntity;
import game.parameters.InputRef.GameKey;

import org.lwjgl.util.vector.Vector2f;

import app.InputManager;

public class Control_HorizontalMove extends ControlMechanism {
	private static final long serialVersionUID = -3011521393718606785L;

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

	public Control_HorizontalMove(ActiveEntity actor, float acceleration,
			float deceleration, float maxSpeed) {
		super(actor);
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public void update() {
		PhysicsInstance physics = actor.getPhysics();
		Vector2f velocity = physics.getVelocity();

		float horizontalSpeed = Math.abs(velocity.x);
		boolean lessThanMaxSpeed = horizontalSpeed < maxSpeed;
		boolean movingLeft = velocity.x < 0;
		boolean notMoving = velocity.x == 0;

		boolean rightPressed = InputManager.getGameKeyState(GameKey.right, 1);
		boolean leftPressed = InputManager.getGameKeyState(GameKey.left, 1);

		if (rightPressed) {
			if (!movingLeft || notMoving) {
				if (lessThanMaxSpeed) {
					physics.addForce(new Vector2f(acceleration, 0f));
				}
			} else {
				physics.addForce(new Vector2f(deceleration, 0f));
			}
		}

		if (leftPressed) {
			if (movingLeft || notMoving) {
				if (lessThanMaxSpeed) {
					physics.addForce(new Vector2f(-acceleration, 0f));
				}
			} else {
				physics.addForce(new Vector2f(-deceleration, 0f));
			}
		}

		// add drag when no key pressed
		if (!leftPressed && !rightPressed && !notMoving) {

			// don't start moving the other direction!
			float maxDecel = deceleration < horizontalSpeed ? deceleration
					: horizontalSpeed;

			if (movingLeft) {
				physics.addForce(new Vector2f(maxDecel, 0f));
			} else {
				physics.addForce(new Vector2f(-maxDecel, 0f));
			}
		}
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
