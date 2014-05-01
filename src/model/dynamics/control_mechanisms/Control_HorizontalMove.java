package model.dynamics.control_mechanisms;

import model.entities.ActiveEntity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import app.InputManager;
import app.InputManager.GameKey;

public class Control_HorizontalMove extends ControlMechanism {
	private static final long serialVersionUID = -3011521393718606785L;

	private float speed;

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Control_HorizontalMove(ActiveEntity actor, float speed) {
		super(actor);
		this.speed = speed;
	}

	@Override
	public void update(float delta) {
		if (InputManager.getGameKeyState(GameKey.right, 1)) {
			actor.physics.addForce(new Vector2f(speed, 0f));
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			actor.physics.addForce(new Vector2f(-speed, 0f));
		}
	}
}
