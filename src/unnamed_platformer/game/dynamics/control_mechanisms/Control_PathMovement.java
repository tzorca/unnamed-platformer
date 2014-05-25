package unnamed_platformer.game.dynamics.control_mechanisms;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.logic.MathHelper;

public class Control_PathMovement extends ControlMechanism {
	private static final long serialVersionUID = 8775296304473034048L;

	private Point[] relativePath = null;
	private double speed = 0;
	private int pathState = 1;

	private Point origin = null;

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Control_PathMovement(ActiveEntity actor, Point origin,
			Point[] relativePath, double speed) {
		super(actor);
		this.origin = origin;
		this.relativePath = relativePath;
		this.speed = speed;
	}
	
	
	public void reset() {
		pathState = 1;
	}

	@Override
	public void update(float multiplier) {
		if (relativePath == null || speed == 0) {
			return;
		}

		Vector2f startPoint = actor.getPos();
		Vector2f targetPoint = new Vector2f(origin.getX() + relativePath[pathState].getX(),
				origin.getX() + relativePath[pathState].getY());

		Vector2f newPoint = MathHelper.moveTowards(startPoint, targetPoint, speed*multiplier);

		if (newPoint.equals(targetPoint)) {
			// We've arrived, so begin moving toward the next node in the path
			pathState++;

			if (pathState > relativePath.length - 1) {
				pathState = 0;
			}
		}

		actor.setPos(newPoint);

	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Point[] getPath() {
		return relativePath;
	}

	public void setPath(Point[] path) {
		this.relativePath = path;
	}

}
