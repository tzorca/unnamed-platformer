

package model.dynamics.control_mechanisms;

import model.Ref.Flag;
import model.entities.ActiveEntity;

import org.lwjgl.util.vector.Vector2f;

import app.InputManager;
import app.InputManager.GameKey;

public class Control_Jump extends ControlMechanism {
	private static final long serialVersionUID = 6634477314813175782L;
	
	long currentDelta, maxDelta;
	double jumpStrength = 0;
	boolean jumping = false;

	public Control_Jump(ActiveEntity actor, double jumpStrength, double maxSeconds) {
		super(actor);
		this.jumpStrength = jumpStrength;
		this.maxDelta = (long) (maxSeconds * 1000);
	}

	@Override
	public void update(float delta) {
		if (!actor.physics.inAir) {

			if (!jumping && InputManager.getGameKeyState(GameKey.up, 1)
					&& actor.checkFlag(Flag.obeysGravity)) {

				beginJumping();
			}
		}

		if (jumping) {
			if (InputManager.getGameKeyState(GameKey.up, 1)) {
				continueJumping(delta);
			} else {
				finishJumping();
			}
		}
	}

	private void finishJumping() {
		jumping = false;
		actor.physics.airTime = 0;
		currentDelta = maxDelta;

		actor.physics.upCancel = false;
	}

	private void continueJumping(float delta) {
		// double jumpInverseTimeRatio = 0;
		if (actor.physics.upCancel) {
			finishJumping();
			return;
		}
		if (currentDelta < maxDelta) {
			actor.physics.addForce(new Vector2f(0f, (float) (-jumpStrength)));
		} else {
			finishJumping();
		}

		currentDelta += delta;
	}

	private void beginJumping() {
		actor.physics.inAir = true;
		jumping = true;
		currentDelta = 0;
	}

	public double getJumpStrength() {
		return jumpStrength;
	}

	public void setJumpStrength(double js) {
		jumpStrength = js;
	}

	public double getJumpTime() {
		return maxDelta / 1000.0;
	}

	public void setJumpTime(double jumpTime) {
		maxDelta = (long) (1000 * jumpTime);
	}
}
