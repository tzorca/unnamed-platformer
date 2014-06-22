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

	public static final float DEFAULT_SPRING_STRENGTH = 10f;

	public static final float DEFAULT_PLR_ACCELERATION = 2.5f;
	public static final float DEFAULT_PLR_DECELERATION = 4f;
	public static final float DEFAULT_PLR_MAX_SPEED = 5.5f;

	public static final float DEFAULT_PLR_JUMP_STRENGTH = 11f;

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
	

	public final static float WATER_SPEED_FACTOR = 0.5f;

	public static final Vector2f EMPTY_VECTOR = new Vector2f(0, 0);

	public static final float GLOBAL_SPEED_LIMIT = 32;

	public static final float DEFAULT_FORCE_MULTIPLIER = 0.9f;
}
