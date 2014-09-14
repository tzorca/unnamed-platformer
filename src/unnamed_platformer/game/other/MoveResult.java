package unnamed_platformer.game.other;

import java.io.Serializable;

public class MoveResult implements Serializable
{
	private static final long serialVersionUID = -5293771597980287464L;

	private boolean xCollision, yCollision;
	private float yAttempt, xAttempt;

	public boolean hadXCollision() {
		return xCollision;
	}

	public boolean hadYCollision() {
		return yCollision;
	}

	public MoveResult(float xAttempt, float yAttempt) {
		xCollision = false;
		yCollision = false;
		this.xAttempt = xAttempt;
		this.yAttempt = yAttempt;
	}

	public boolean hadAnyCollision() {
		return xCollision || yCollision;
	}

	public float getyAttempt() {
		return yAttempt;
	}

	public float getxAttempt() {
		return xAttempt;
	}

	public void setXCollision(boolean value) {
		xCollision = true;
	}

	public void setYCollision(boolean value) {
		yCollision = true;
	}

}
