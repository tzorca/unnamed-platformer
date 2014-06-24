package unnamed_platformer.game.ctrl_methods;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.ActiveEntity;

public class Control_PathMovement extends ControlMechanism {
	private static final long serialVersionUID = 8775296304473034048L;

	private List<Vector2f> relativePath = new ArrayList<Vector2f>();
	private double speed = 0;
	private int pathIndex = 0;
	boolean loop = false;

	private Vector2f origin = null;

	private boolean finished;

	public Vector2f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2f origin) {
		this.origin = origin;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public Control_PathMovement(ActiveEntity actor, Vector2f startPos, List<Vector2f> relativePath, double speed) {
		super(actor);
		this.origin = startPos;
		this.relativePath = relativePath;
		this.speed = speed;
	}

	public void reset() {
		pathIndex = 0;
		actor.setPos(origin);
		finished = false;
	}

	@Override
	public void doUpdate(float multiplier) {
		if (finished) {
			return;
		}
		if (relativePath == null || speed == 0) {
			return;
		}

		Vector2f startPoint = actor.getPos();
		Vector2f targetPoint = new Vector2f(origin.getX() + relativePath.get(pathIndex).getX(), origin.getX()
				+ relativePath.get(pathIndex).getY());

		Vector2f newPoint = MathHelper.moveTowards(startPoint, targetPoint, speed * multiplier);

		if (newPoint.equals(targetPoint)) {
			// We've arrived, so begin moving toward the next node in the path
			pathIndex++;

			if (pathIndex > relativePath.size() - 1) {
				pathIndex = 0;

				if (!loop) {
					finished = true;
				}
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

	public List<Vector2f> getPath() {
		return relativePath;
	}

	public void setPath(List<Vector2f> path) {
		this.relativePath = path;
	}

}
