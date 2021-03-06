package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.physics.PhysicsInstance;
import unnamed_platformer.globals.GameGlobals.Flag;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;

public class Ctrl_Jump extends ControlMechanism
{

	float jumpStrength = 0;
	boolean jumping = false;

	public Ctrl_Jump(ActiveEntity actor, float jumpStrength) {
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

		if (!jumping && physics.isOnGround()
				&& InputManager.keyIsPressed(GameKey.A, 1)
				&& actor.isFlagSet(Flag.OBEYS_GRAVITY)) {

			jump(multiplier);
		}

		if (jumping) {
			if (!InputManager.keyIsPressed(GameKey.A, 1)) {

				if (physics.getVelocity().y < 0) {
					float yDiff = physics.getVelocity().y + jumpStrength;
					if (yDiff > 0) {

						actor.getPhysics().addForce(
								new Vector2f(0, (float) (yDiff
										* Math.pow(multiplier, 0.5) / 4)));
					}
				}
			}
		}
	}

	private void jump(float multiplier) {
		AudioManager.playSample("jump");
		actor.getPhysics().setInAir(true);
		jumping = true;

		actor.getPhysics().addForce(
				new Vector2f(0f, (float) (-jumpStrength * Math.pow(multiplier,
						0.5))));
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
