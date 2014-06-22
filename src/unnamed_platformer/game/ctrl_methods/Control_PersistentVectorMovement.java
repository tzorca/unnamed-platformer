package unnamed_platformer.game.ctrl_methods;

import java.awt.Point;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.entities.ActiveEntity;

public class Control_PersistentVectorMovement extends ControlMechanism {
	private static final long serialVersionUID = -7737109613423428367L;

	private Vector2f vector;
	private long endTime = 0;

	private Point origin = null;

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Control_PersistentVectorMovement(ActiveEntity actor, double speed,
			double angle) {
		super(actor);
		this.vector = MathHelper.vectorFromAngleAndSpeed(speed, angle);
	}

	public Control_PersistentVectorMovement(ActiveEntity actor,
			Vector2f vector, long duration) {
		super(actor);
		this.vector = vector;
		this.endTime = TimeManager.time() + duration;
	}

	@Override
	public void update(float multiplier) {
		Vector2f vectorMul = new Vector2f(vector.x * multiplier, vector.y
				* multiplier);
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
