package model.dynamics.control_mechanisms;

import model.entities.ActiveEntity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import app.InputManager;

public class Control_OverheadMove extends ControlMechanism {
	private static final long serialVersionUID = 8927418630246157688L;

	private float speed;

	public Control_OverheadMove(ActiveEntity actor, float speed) {
		super(actor);
		this.speed = speed;
	}

	@Override
	public void update(float delta) {
		if (InputManager.getKeyState(Keyboard.KEY_RIGHT)) {
			actor.physics.addForce(new Vector2f(speed, 0));
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			actor.physics.addForce(new Vector2f(-speed, 0));
		}

		if (InputManager.getKeyState(Keyboard.KEY_UP)) {
			actor.physics.addForce(new Vector2f(0, -speed));
		}

		if (InputManager.getKeyState(Keyboard.KEY_DOWN)) {
			actor.physics.addForce(new Vector2f(0, speed));
		}

	}
}
