package game.dynamics.control_mechanisms;

import game.entities.ActiveEntity;
import game.parameters.InputRef.GameKey;

import org.lwjgl.util.vector.Vector2f;

import app.InputManager;

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
	public void update(long millisecDelta) {
		if (InputManager.getGameKeyState(GameKey.right, 1)) {
			actor.getPhysics().addForce(new Vector2f(speed, 0f));
		}

		if (InputManager.getGameKeyState(GameKey.left, 1)) {
			actor.getPhysics().addForce(new Vector2f(-speed, 0f));
		}
	}
	
	public void reset() {
		
	}
}
