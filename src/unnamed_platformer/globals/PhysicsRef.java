package unnamed_platformer.globals;

import org.newdawn.slick.geom.Vector2f;

public class PhysicsRef {


	public enum Orientation {
		UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT
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

	public static final double STRICT_SIDE_MATCH_DISTANCE = Math.PI / 6;
	public static final double LOOSE_SIDE_MATCH_DISTANCE = Math.PI / 2;

	public static final Vector2f EMPTY_VECTOR = new Vector2f(0, 0);


}
