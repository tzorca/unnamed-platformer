package unnamed_platformer.game.ctrl_methods;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.globals.InputRef.GameKey;



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
