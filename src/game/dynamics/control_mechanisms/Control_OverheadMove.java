package game.dynamics.control_mechanisms;

import game.entities.ActiveEntity;
import game.parameters.InputRef.GameKey;

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
	public void update() {
		if (InputManager.getGameKeyState(GameKey.right, 1)) {
			actor.getPhysics().addForce(new Vector2f(speed, 0));
		}

		if (InputManager.getGameKeyState(GameKey.left, 1)) {
			actor.getPhysics().addForce(new Vector2f(-speed, 0));
		}

		if (InputManager.getGameKeyState(GameKey.up, 1)) {
			actor.getPhysics().addForce(new Vector2f(0, -speed));
		}

		if (InputManager.getGameKeyState(GameKey.down, 1)) {
			actor.getPhysics().addForce(new Vector2f(0, speed));
		}

	}

	public void reset() {
	}
}
