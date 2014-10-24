package unnamed_platformer.game.behaviours;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.ActiveEntity;

public class Ctrl_PathMovement extends ControlMechanism
{
	private List<Vector2f> relativePath = new ArrayList<Vector2f>();
	private double speed = 0;
	private int pathIndex = 0;
	boolean doLoop = false;

	private Vector2f origin = null;

	private boolean finished;

	public Vector2f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2f origin) {
		this.origin = origin;
	}

	public void setLoop(boolean loop) {
		this.doLoop = loop;
	}

	public Ctrl_PathMovement(ActiveEntity actor, Vector2f startPos,
			List<Vector2f> relativePath, double speed) {
		super(actor);
		this.origin = startPos;
		this.relativePath = relativePath;
		this.speed = speed;
		reset();
	}

	@Override
	public void reset() {
		actor.setPos(origin);
		pathIndex = 0;
		finished = false;
	}

	@Override
	public void doUpdate(float multiplier) {
		if (finished || relativePath == null || speed == 0) {
			return;
		}

		Vector2f nextLocation = new Vector2f(origin).add(relativePath
				.get(pathIndex));

		double distanceForThisTic = speed * multiplier;

		int timeout = 100;
		while (distanceForThisTic > 1 || timeout <= 0) {
			timeout--;

			Vector2f posBeforeMove = new Vector2f(actor.getPos());
			actor.setPos(MathHelper.moveTowards(actor.getPos(), nextLocation,
					(float) (speed * multiplier)));
			distanceForThisTic -= posBeforeMove.distance(actor.getPos());

			if (actor.getPos().distance(nextLocation) < speed * multiplier) {
				pathIndex++;
				if (pathIndex >= relativePath.size()) {
				
					pathIndex = 0;
					if (!doLoop) {
						finished = true;
						break;
					}
				}
				nextLocation = new Vector2f(origin).add(relativePath
						.get(pathIndex));
			}
		}

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
