// Tim Zorca
// CPSC 3520
package model.behaviours;

import model.Ref.Flag;
import model.entities.ActiveEntity;

import org.lwjgl.util.vector.Vector2f;

import app.InputManager;
import app.InputManager.GameKey;

public class Input_Jump extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6634477314813175782L;
	long currentDelta, maxDelta;
	double jumpStrength = 0;
	boolean jumping = false;

	public Input_Jump(double jumpStrength, double maxSeconds) {
		this.jumpStrength = jumpStrength;
		this.maxDelta = (long) (maxSeconds * 1000);
	}

	@Override
	public void run(ActiveEntity target, float delta) {
		if (!target.inAir) {

			if (!jumping && InputManager.getGameKeyState(GameKey.up, 1)
					&& target.checkFlag(Flag.obeysGravity)) {

				beginJumping(target);
			}
		}

		if (jumping) {
			if (InputManager.getGameKeyState(GameKey.up, 1)) {
				continueJumping(target, delta);
			} else {
				finishJumping(target);
			}
		}
	}

	private void finishJumping(ActiveEntity jumper) {
		jumping = false;
		jumper.airTime = 0;
		currentDelta = maxDelta;

		jumper.upCancel = false;
	}

	private void continueJumping(ActiveEntity jumper, float delta) {
		// double jumpInverseTimeRatio = 0;
		if (jumper.upCancel) {
			finishJumping(jumper);
			return;
		}
		if (currentDelta < maxDelta) {
			jumper.addForce(new Vector2f(0f, (float) (-jumpStrength)));
		} else {
			finishJumping(jumper);
		}

		currentDelta += delta;
	}

	private void beginJumping(ActiveEntity jumper) {
		jumper.inAir = true;
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
