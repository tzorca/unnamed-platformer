package model.dynamics.control_mechanisms;

import java.awt.Point;

import model.entities.ActiveEntity;
import model.logic.MathHelper;

import org.lwjgl.util.vector.Vector2f;

import app.TimeManager;

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
		System.out
				.println("endtime = " + TimeManager.time() + " + " + duration);
	}

	@Override
	public void update(float delta) {
		actor.physics.addForce(vector);
		if (endTime > 0 && TimeManager.time() > endTime) {
			System.out.println("time > endTime (" + TimeManager.time() + ")");
			finish();
		}

		if (actor.physics.solidCollisionOccurred == true) {
			finish();
		}
	}

	public void finish() {
		this.toRemove = true;
	}

}
