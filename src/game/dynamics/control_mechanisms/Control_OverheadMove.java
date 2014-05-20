package game.dynamics.control_mechanisms;

import org.newdawn.slick.geom.Vector2f;

import game.entities.ActiveEntity;
import game.parameters.InputRef.GameKey;


import app.InputManager;

public class Control_OverheadMove extends ControlMechanism {
	private static final long serialVersionUID = 8927418630246157688L;

	private float speed;

	public Control_OverheadMove(ActiveEntity actor, float speed) {
		super(actor);
		this.speed = speed;
	}

	@Override
	public void update(float multiplier) {
		float mulSpeed = speed * multiplier;
		
		if (InputManager.getGameKeyState(GameKey.right, 1)) {
			actor.getPhysics().addForce(new Vector2f(mulSpeed, 0));
		}

		if (InputManager.getGameKeyState(GameKey.left, 1)) {
			actor.getPhysics().addForce(new Vector2f(-mulSpeed, 0));
		}

		if (InputManager.getGameKeyState(GameKey.up, 1)) {
			actor.getPhysics().addForce(new Vector2f(0, -mulSpeed));
		}

		if (InputManager.getGameKeyState(GameKey.down, 1)) {
			actor.getPhysics().addForce(new Vector2f(0, mulSpeed));
		}

	}

	public void reset() {
	}
}
