// Tim Zorca
// CPSC 3520
package model.behaviours;

import java.awt.Point;

import model.entities.ActiveEntity;
import model.logic.MathHelper;

import org.lwjgl.util.vector.Vector2f;

public class PersistentVectorMovement extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7737109613423428367L;
	private Vector2f vector;
	private Float duration=null, elapsed=0f;

	private Point origin = null;

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public PersistentVectorMovement(double speed, double angle) {
		this.vector = MathHelper.vectorFromAngleAndSpeed(speed, angle);
	}
	public PersistentVectorMovement(Vector2f vector, float duration) {
		this.vector = vector;
		this.duration = duration;
	}
	

	@Override
	public void run(ActiveEntity targetObj, float delta) {
		targetObj.addForce(vector);
		if (duration != null) {
			
			elapsed += delta;
			if (elapsed > duration) {
				finish(targetObj);
			}
		}
		if (targetObj.lastTicSolidCollision) {
			finish(targetObj);
		}
	}
	
	public void finish(ActiveEntity targetObj) {
		this.toRemove =true;
	}

}
