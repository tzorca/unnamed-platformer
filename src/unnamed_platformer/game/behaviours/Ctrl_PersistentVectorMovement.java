package unnamed_platformer.game.behaviours;

import java.awt.Point;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.entities.ActiveEntity;

public class Ctrl_PersistentVectorMovement extends ControlMechanism {

	private Vector2f vector;
	private long endTime = 0;

	private Point origin = null;

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Ctrl_PersistentVectorMovement(ActiveEntity actor, double speed, double angle) {
		super(actor);
		this.vector = MathHelper.vectorFromAngleAndSpeed(speed, angle);
	}

	public Ctrl_PersistentVectorMovement(ActiveEntity actor, Vector2f vector, long duration) {
		super(actor);
		this.vector = vector;
		this.endTime = TimeManager.time() + duration;
	}

	@Override
	public void doUpdate(float multiplier) {
		Vector2f vectorMul = new Vector2f(vector.x * multiplier, vector.y * multiplier);
		actor.getPhysics().addForce(vectorMul);

		if (endTime > 0 && TimeManager.time() > endTime) {
			finish();
		}

		if (actor.getPhysics().lastMoveResult.hadAnyCollision()) {
			finish();
		}
	}

	public void finish() {
		this.toRemove = true;
	}

	public void reset() {
	}

}
