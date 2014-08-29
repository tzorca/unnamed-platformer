package unnamed_platformer.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public final class MathHelper
{

	public enum Axis {
		BOTH, HORIZONTAL, VERTICAL, NONE
	}

	public enum Orientation {
		DOWN, DOWN_LEFT, DOWN_RIGHT, LEFT, RIGHT, UP, UP_LEFT, UP_RIGHT
	}

	public enum Side {
		BOTTOM(Math.PI / 2), LEFT(Math.PI), RIGHT(0), TOP(-Math.PI / 2);

		private final double radians;

		Side(double radians) {
			this.radians = radians;
		}

		public double getRadians() {
			return radians;
		}
	}

	public static final Vector2f EMPTY_VECTOR = new Vector2f(0, 0);

	public static final double MAX_RAD = 2 * Math.PI;
	public static final double MIN_RAD = -2 * Math.PI;

	public static double angleFromVector(final Vector2f vector) {
		return Math.atan2(vector.y, vector.x);
	}

	public static List<Vector2f> createCirclePath(final float radius) {
		final List<Vector2f> path = new ArrayList<Vector2f>();
		final Vector2f shiftVector = new Vector2f(0, -radius);
		final Vector2f initialVector = new Vector2f(shiftVector);
		for (int i = 0; i <= 360; i++) {
			initialVector.setTheta(i);
			path.add(new Vector2f(initialVector));
		}

		return path;
	}

	public static List<Vector2f> createUpDownPath(final float dist) {
		final List<Vector2f> path = new ArrayList<Vector2f>();
		path.add(new Vector2f(0, 0));
		path.add(new Vector2f(0, -dist));
		path.add(new Vector2f(0, 0));

		return path;
	}

	public static double getArea(final Rectangle rect) {
		return rect.getWidth() * rect.getHeight();
	}

	public static Double getIntersectionAngle(final Rectangle rectA, final Rectangle rectB) {
		final Shape[] unionResult = rectA.union(rectB);
		Shape c = new Rectangle(0, 0, 0, 0);
		if (unionResult.length > 0) {
			c = unionResult[0];
		}

		final float intersectionX = (float) (c.getCenterX() - rectA.getCenterX());
		final float intersectionY = (float) (c.getCenterY() - rectA.getCenterY());
		final Vector2f intersectionVector = new Vector2f(intersectionX, intersectionY);

		return angleFromVector(intersectionVector);
	}

	public static float getRectInnerRadius(final float width, final float height) {
		return (width < height ? width : height) / 2;
	}

	public static EnumMap<Side, Double> getSideDistances(final Double radians, final Side[] sidesToCheck) {
		final EnumMap<Side, Double> distances = new EnumMap<Side, Double>(Side.class);

		for (Side side : sidesToCheck) {
			Double dist = Math.abs(side.getRadians() - radians);
			dist = wrapValue(dist, MathHelper.MIN_RAD, MathHelper.MAX_RAD);

			distances.put(side, dist);
		}

		return distances;
	}

	// Move in a direction towards a point (but not past it)
	public static Vector2f moveTowards(final Vector2f startPoint, final Vector2f targetPoint, final double speed) {

		final float initialDist = startPoint.distance(targetPoint);
		final double theta = Math.atan2(targetPoint.y - startPoint.y, targetPoint.x - startPoint.x);
		final float movX = (float) (speed * Math.cos(theta));
		final float movY = (float) (speed * Math.sin(theta));

		Vector2f newPoint = new Vector2f(startPoint.getX() + movX, startPoint.getY() + movY);

		final double newDist = newPoint.distance(targetPoint);

		// don't move past the point
		if (newDist >= initialDist) {
			newPoint = targetPoint;
		}

		return newPoint;
	}

	public static Object randInArray(final Object[] array) {
		return array[randRange(0, array.length - 1)];
	}

	public static Object randInCollection(final Collection<?> collection) {
		return new ArrayList<>(collection).get(randRange(0, collection.size() - 1));
	}

	public static Object randInList(final List<?> list) {
		return list.get(randRange(0, list.size() - 1));
	}

	public static Object randInSet(final Set<?> set) {
		return new ArrayList<Object>(set).get(randRange(0, set.size() - 1));
	}

	public static Object randKeyInMap(final Map<?, ?> map) {
		return randInSet(map.keySet());
	}

	// Return a random number inside the range [low, high]
	public static int randRange(final int low, final int high) {
		return low + (int) (Math.random() * (high - low + 1));
	}

	public static double randSet(final double values[]) {
		return values[(int) (Math.random() * (values.length - 1 + 1))];
	}

	public static String randSet(final String values[]) {
		return values[(int) (Math.random() * (values.length - 1 + 1))];
	}

	public static Object randValueInMap(final Map<?, ?> map) {
		return randInCollection(map.values());
	}

	public static Vector2f snapToGrid(final Vector2f vector2f, final int gridSize) {
		final int xPos = (int) ((int) vector2f.x / gridSize) * gridSize;
		final int yPos = (int) ((int) vector2f.y / gridSize) * gridSize;

		return new Vector2f(xPos, yPos);
	}

	public static Vector2f vectorFromAngleAndSpeed(final double speed, final double angle) {
		return new Vector2f((float) (speed * Math.cos(angle)), (float) (speed * Math.sin(angle)));
	}

	public static Vector2f vectorFromOrientationAndLength(final Orientation orientation, final float length) {
		switch (orientation) {
		case DOWN:
			return new Vector2f(0, length);
		case DOWN_LEFT:
			return new Vector2f(-length / 2, length / 2);
		case DOWN_RIGHT:
			return new Vector2f(length / 2, length / 2);
		case LEFT:
			return new Vector2f(-length, 0);
		case RIGHT:
			return new Vector2f(length, 0);
		case UP:
			return new Vector2f(0, -length);
		case UP_LEFT:
			return new Vector2f(-length / 2, -length / 2);
		case UP_RIGHT:
			return new Vector2f(length / 2, -length / 2);
		default:
			return null;
		}
	}

	public static java.awt.Point vectorToPoint(final Vector2f vector) {
		return new java.awt.Point((int) vector.x, (int) vector.y);
	}

	public static double wrapValue(final double value, final double min, final double max) {
		if (value > max) {
			return value - max + min;
		} else if (value < min) {
			return max - (min - value);
		}
		return value;
	}

}
