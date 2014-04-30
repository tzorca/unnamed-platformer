

package model.behaviours;

import model.entities.ActiveEntity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import app.InputManager;

public class Input_OverheadMove extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8927418630246157688L;
	private float speed;

	public Input_OverheadMove(float speed) {
		this.speed = speed;
	}

	@Override
	public void run(ActiveEntity target, float delta) {
		if (InputManager.getKeyState(Keyboard.KEY_RIGHT)) {
			target.addForce(new Vector2f(speed, 0));
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			target.addForce(new Vector2f(-speed, 0));
		}

		if (InputManager.getKeyState(Keyboard.KEY_UP)) {
			target.addForce(new Vector2f(0, -speed));
		}

		if (InputManager.getKeyState(Keyboard.KEY_DOWN)) {
			target.addForce(new Vector2f(0, speed));
		}

	}
}
