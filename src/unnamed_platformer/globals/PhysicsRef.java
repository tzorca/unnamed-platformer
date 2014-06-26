package unnamed_platformer.globals;

import org.newdawn.slick.geom.Vector2f;

public class PhysicsRef {

	public static Vector2f gravity = new Vector2f(0, 0.23f);

	public enum Orientation {
		UP, DOWN, LEFT, RIGHT, UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT
	}

	public enum Axis {
		HORIZONTAL, VERTICAL, NONE
	}

	public enum Side {
		TOP(-Math.PI / 2), RIGHT(0), LEFT(Math.PI), BOTTOM(Math.PI / 2);

		private final double radians;

		Side(double radians) {
			this.radians = radians;
		}

		public double getRadians() {
			return radians;
		}
	}

	public static final double MIN_RAD = -2 * Math.PI;
	public static final double MAX_RAD = 2 * Math.PI;

	public static final double STRICT_SIDE_MATCH_DISTANCE = Math.PI / 2;
	public static final double LOOSE_SIDE_MATCH_DISTANCE = Math.PI / 2.5;

	public static final Vector2f EMPTY_VECTOR = new Vector2f(0, 0);

	public static final float GLOBAL_SPEED_LIMIT = 32;

	public static final float DEFAULT_FORCE_MULTIPLIER = 0.9f;

}
