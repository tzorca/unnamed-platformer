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

import unnamed_platformer.game.physics.DirectionalEnums.Orientation;
import unnamed_platformer.game.physics.DirectionalEnums.Side;

public final class MathHelper
{

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

	public static List<Vector2f> createRightLeftPath(final float dist) {
		final List<Vector2f> path = new ArrayList<Vector2f>();
		path.add(new Vector2f(0, 0));
		path.add(new Vector2f(dist, 0));
		path.add(new Vector2f(0, 0));
		return path;
	}

	public static double getArea(final Rectangle rect) {
		return rect.getWidth() * rect.getHeight();
	}
	
	public static Vector2f rectACenterMinusRectBCenter(final Rectangle rectA, final Rectangle rectB) {
		Vector2f pointA = new Vector2f(rectA.getCenterX(), rectA.getCenterY());
		Vector2f pointB = new Vector2f(rectB.getCenterX(), rectB.getCenterY());
		
		return new Vector2f(pointA).sub(pointB);
	}

	public static Double getIntersectionAngle(final Rectangle rectA,
			final Rectangle rectB) {
		final Shape[] unionResult = rectA.union(rectB);
		Shape c = new Rectangle(0, 0, 0, 0);
		if (unionResult.length > 0) {
			c = unionResult[0];
		}

		final float intersectionX = (float) (c.getCenterX() - rectA
				.getCenterX());
		final float intersectionY = (float) (c.getCenterY() - rectA
				.getCenterY());
		final Vector2f intersectionVector = new Vector2f(intersectionX,
				intersectionY);

		return angleFromVector(intersectionVector);
	}

	public static float getRectInnerRadius(final float width, final float height) {
		return (width < height ? width : height) / 2;
	}

	public static EnumMap<Side, Double> getSideDistances(final Double radians,
			final Side[] sidesToCheck) {
		final EnumMap<Side, Double> distances = new EnumMap<Side, Double>(
				Side.class);

		for (Side side : sidesToCheck) {
			Double dist = Math.abs(side.getRadians() - radians);
			dist = wrapValue(dist, MathHelper.MIN_RAD, MathHelper.MAX_RAD);

			distances.put(side, dist);
		}

		return distances;
	}

	// Move in a direction towards a point (but not past it)
	public static Vector2f moveTowards(final Vector2f startPoint,
			final Vector2f targetPoint, final float speed) {

		return new Vector2f(startPoint).add(new Vector2f(targetPoint)
				.sub(new Vector2f(startPoint)).normalise().scale(speed));

	}

	public static Object randInArray(final Object[] array) {
		return array[randRange(0, array.length - 1)];
	}

	public static Object randInCollection(final Collection<?> collection) {
		return new ArrayList<>(collection).get(randRange(0,
				collection.size() - 1));
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

	public static Vector2f snapToGrid(final Vector2f vector2f,
			final int gridSize) {
		final int xPos = (int) ((int) vector2f.x / gridSize) * gridSize;
		final int yPos = (int) ((int) vector2f.y / gridSize) * gridSize;

		return new Vector2f(xPos, yPos);
	}

	public static Vector2f vectorFromAngleAndSpeed(final double speed,
			final double angle) {
		return new Vector2f((float) (speed * Math.cos(angle)),
				(float) (speed * Math.sin(angle)));
	}

	public static Vector2f vectorFromOrientationAndLength(
			final Orientation orientation, final float length) {
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

	public static double wrapValue(final double value, final double min,
			final double max) {
		if (value > max) {
			return value - max + min;
		} else if (value < min) {
			return max - (min - value);
		}
		return value;
	}

	public static java.awt.Rectangle slickToJavaRect(Rectangle slickRect) {
		int x = (int) slickRect.getX();
		int y = (int) slickRect.getY();
		int w = (int) slickRect.getWidth();
		int h = (int) slickRect.getHeight();
		return new java.awt.Rectangle(x, y, w, h);
	}

	public static boolean rectExitingOrOutsideRect(Rectangle activeRect,
			Rectangle largeRect) {
		if (activeRect.getMinX() <= largeRect.getMinX()) {
			return true;
		}
		if (activeRect.getMaxX() >= largeRect.getMaxX()) {
			return true;
		}
		if (activeRect.getMinY() <= largeRect.getMinY()) {
			return true;
		}
		if (activeRect.getMaxY() >= largeRect.getMaxY()) {
			return true;
		}
		return false;
	}

	public static float getSpeed(Vector2f vector) {
		return vector.distance(new Vector2f(0, 0));
	}

}
