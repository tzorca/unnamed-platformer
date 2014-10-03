package unnamed_platformer.game.behaviours;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.ActiveEntity;

public class Ctrl_PathMovement extends ControlMechanism {
	private List<Vector2f> relativePath = new ArrayList<Vector2f>();
	private double speed = 0;
	private int pathIndex = 0;
	boolean loop = false;
	Vector2f targetPoint = new Vector2f();

	private Vector2f origin = null;

	private boolean finished;

	private int lastPathIndex;

	public Vector2f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2f origin) {
		this.origin = origin;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public Ctrl_PathMovement(ActiveEntity actor, Vector2f startPos,
			List<Vector2f> relativePath, double speed) {
		super(actor);
		this.origin = startPos;
		System.out.println("Starting at " + startPos);
		this.relativePath = relativePath;
		this.speed = speed;
		reset();
	}

	public void reset() {
		setPathIndex(0);
		actor.setPos(origin);
		finished = false;
	}

	@Override
	public void doUpdate(float multiplier) {
		if (finished || relativePath == null || speed == 0) {
			return;
		}

		Vector2f origPoint = actor.getPos();
		Vector2f newPoint = actor.getPos();

		double requiredDist = speed * multiplier;
		do {
			newPoint = MathHelper.moveTowards(actor.getPos(), targetPoint,
					requiredDist);
			requiredDist -= newPoint.distance(origPoint);

			actor.setPos(newPoint);

			if (newPoint.x == targetPoint.x && newPoint.y == targetPoint.y) {
				if (pathIndex == 0 && lastPathIndex == relativePath.size() - 1
						&& !loop) { 
					finished = true;
					return;
				}

				setPathIndex(pathIndex + 1);
			}

		} while (requiredDist > 0);

	}

	private void setPathIndex(int index) {
		if (index >= relativePath.size()) {
			index = 0;
		}
		lastPathIndex = pathIndex;
		pathIndex = index;
		targetPoint = new Vector2f(origin);
		targetPoint.x += relativePath.get(pathIndex).getX();
		targetPoint.y += relativePath.get(pathIndex).getY();
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
