// Tim Zorca
// CPSC 3520
package model.behaviours;

import model.entities.ActiveEntity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import app.InputManager;
import app.InputManager.GameKey;

public class Input_HorizontalMove extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3011521393718606785L;
	private float speed;

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Input_HorizontalMove(float speed) {
		this.speed = speed;
	}

	@Override
	public void run(ActiveEntity target, float delta) {
		if (InputManager.getGameKeyState(GameKey.right, 1)) {
			target.addForce(new Vector2f(speed, 0f));
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			target.addForce(new Vector2f(-speed, 0f));
		}
	}
}
