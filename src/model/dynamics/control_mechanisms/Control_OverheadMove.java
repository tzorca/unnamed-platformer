package model.dynamics.control_mechanisms;

import model.entities.ActiveEntity;
import model.parameters.InputRef.GameKey;

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
	public void update(long delta) {
		if (InputManager.getGameKeyState(GameKey.right, 1)) {
			actor.physics.addForce(new Vector2f(speed, 0));
		}

		if (InputManager.getGameKeyState(GameKey.left, 1)) {
			actor.physics.addForce(new Vector2f(-speed, 0));
		}

		if (InputManager.getGameKeyState(GameKey.up, 1)) {
			actor.physics.addForce(new Vector2f(0, -speed));
		}

		if (InputManager.getGameKeyState(GameKey.down, 1)) {
			actor.physics.addForce(new Vector2f(0, speed));
		}

	}
}
