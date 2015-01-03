package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;

public class Ctrl_OverheadMove extends ControlMechanism {

	private float speed;

	public Ctrl_OverheadMove(ActiveEntity actor, float speed) {
		super(actor);
		this.speed = speed;
	}

	@Override
	public void doUpdate(float multiplier) {
		float mulSpeed = speed * multiplier;

		if (InputManager.keyIsPressed(GameKey.RIGHT, 1)) {
			actor.getPhysics().addForce(new Vector2f(mulSpeed, 0));
		}

		if (InputManager.keyIsPressed(GameKey.LEFT, 1)) {
			actor.getPhysics().addForce(new Vector2f(-mulSpeed, 0));
		}

		if (InputManager.keyIsPressed(GameKey.UP, 1)) {
			actor.getPhysics().addForce(new Vector2f(0, -mulSpeed));
		}

		if (InputManager.keyIsPressed(GameKey.DOWN, 1)) {
			actor.getPhysics().addForce(new Vector2f(0, mulSpeed));
		}

	}

	public void reset() {
	}
}
