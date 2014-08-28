package unnamed_platformer.game.ctrl_methods;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.game.PhysicsInstance;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.InputRef.GameKey;

public class Control_Jump extends ControlMechanism {
	private static final long serialVersionUID = 6634477314813175782L;

	float jumpStrength = 0;
	boolean jumping = false;

	public Control_Jump(ActiveEntity actor, float jumpStrength) {
		super(actor);
		this.jumpStrength = jumpStrength;
	}

	@Override
	public void doUpdate(float multiplier) {
		PhysicsInstance physics = actor.getPhysics();

		if (physics.lastMoveResult.hadYCollision()
				&& physics.lastMoveResult.getyAttempt() > 0) {
			jumping = false;
		}

		if (!jumping && physics.isOnGround() && InputManager.getGameKeyState(GameKey.up, 1)
				&& actor.isFlagSet(Flag.OBEYS_GRAVITY)) {

			actor.getPhysics().setInAir(true);
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
