package game.dynamics.control_mechanisms;

import game.PhysicsInstance;
import game.entities.ActiveEntity;
import game.parameters.InputRef.GameKey;
import game.parameters.Ref.Flag;

import org.newdawn.slick.geom.Vector2f;

import app.InputManager;

public class Control_Jump extends ControlMechanism {
	private static final long serialVersionUID = 6634477314813175782L;

	float jumpStrength = 0;
	boolean jumping = false;

	public Control_Jump(ActiveEntity actor, float jumpStrength) {
		super(actor);
		this.jumpStrength = jumpStrength;
	}

	@Override
	public void update(float multiplier) {
		PhysicsInstance physics = actor.getPhysics();

		if (physics.lastMoveResult.hadYCollision()
				&& physics.lastMoveResult.getyAttempt() > 0) {
			jumping = false;
		}

		if (!jumping && InputManager.getGameKeyState(GameKey.up, 1)
				&& actor.isFlagSet(Flag.obeysGravity)) {

			actor.getPhysics().inAir = true;
			jumping = true;

			actor.getPhysics().addForce(
					new Vector2f(0f, (float) (-jumpStrength * Math.pow(multiplier, 0.5))));
		}

		if (jumping) {
			if (!InputManager.getGameKeyState(GameKey.up, 1)) {

				if (physics.getVelocity().y < 0) {
					float yDiff = physics.getVelocity().y + jumpStrength;
					if (yDiff > 0) {

						actor.getPhysics().addForce(
								new Vector2f(0, (float) (yDiff * Math.pow(multiplier, 0.5)
										/ 4)));
					}
				}
			}
		}
	}

	public void reset() {
		jumping = false;
	}

	public double getJumpStrength() {
		return jumpStrength;
	}

	public void setJumpStrength(float js) {
		jumpStrength = js;
	}

}
