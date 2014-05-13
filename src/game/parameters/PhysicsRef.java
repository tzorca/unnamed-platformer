package game.parameters;

import org.lwjgl.util.vector.Vector2f;

public class PhysicsRef {

	public static Vector2f gravity = new Vector2f(0, 0.6f);

	public enum Orientation {
		UP, DOWN, LEFT, RIGHT, UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT
	}

	public enum Axis {
		HORIZONTAL, VERTICAL
	}

	public static final float DEFAULT_SPRING_STRENGTH = 0.3f;

	public static final float DEFAULT_PLR_SPEED = 0.45f;
	public static final float DEFAULT_PLR_JUMP_STRENGTH = 0.7f;
	public static final float DEFAULT_PLR_JUMP_TIME = 0.5f;

	public static final float DEFAULT_SHOOT_SPEED = 0.9f;
	public static final float DEFAULT_SHOOT_DELAY = 100;

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
}
