

package model.behaviours;

import java.awt.Point;

import model.entities.ActiveEntity;
import model.logic.MathHelper;

public class PathMovement extends Behaviour {

	/**
	 * 
	 */
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

	public PathMovement(Point origin, Point[] relativePath,
			double speed) {
		this.origin = origin;
		this.relativePath = relativePath;
		this.speed = speed;
	}

	@Override
	public void run(ActiveEntity targetObj, float delta) {
		if (relativePath == null || speed == 0) {
			return;
		}

		Point startPoint = targetObj.getPos();
		Point targetPoint = new Point(origin.x + relativePath[pathState].x,
				origin.y + relativePath[pathState].y);

		Point newPoint = MathHelper.moveTowards(startPoint, targetPoint, speed);

		if (newPoint.equals(targetPoint)) {
			// We've arrived, so begin moving toward the next node in the path
			pathState++;

			if (pathState > relativePath.length - 1) {
				pathState = 0;
			}
		}

		targetObj.setPos(newPoint);

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
