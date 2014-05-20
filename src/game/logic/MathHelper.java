package game.logic;

import game.parameters.PhysicsRef;
import game.parameters.PhysicsRef.Orientation;
import game.parameters.PhysicsRef.Side;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class MathHelper {

	// Move in a direction towards a point (but not past it)
	public static Vector2f moveTowards(Vector2f startPoint,
			Vector2f targetPoint, double speed) {

		float initialDist = startPoint.distance(targetPoint);
		double theta = Math.atan2(targetPoint.y - startPoint.y, targetPoint.x
				- startPoint.x);
		float movX = (float) (speed * Math.cos(theta));
		float movY = (float) (speed * Math.sin(theta));

		Vector2f newPoint = new Vector2f(startPoint.getX() + movX,
				startPoint.getY() + movY);

		double newDist = newPoint.distance(targetPoint);

		// don't move past the point
		if (newDist >= initialDist) {
			newPoint = targetPoint;
		}

		return newPoint;
	}

	public static Vector2f snapToGrid(Vector2f vector2f, int gridSize) {
		int x = (int) ((int) vector2f.x / gridSize) * gridSize;
		int y = (int) ((int) vector2f.y / gridSize) * gridSize;

		return new Vector2f(x, y);
	}

	public static Vector2f vectorFromAngleAndSpeed(double speed, double angle) {
		return new Vector2f((float) (speed * Math.cos(angle)),
				(float) (speed * Math.sin(angle)));
	}

	public static Double getIntersectionAngle(Rectangle a, Rectangle b) {
		Shape[] unionResult = a.union(b);
		Shape c = new Rectangle(0, 0, 0, 0);
		if (unionResult.length > 0) {
			c = unionResult[0];
		}

		float intersectionX = (float) (c.getCenterX() - a.getCenterX());
		float intersectionY = (float) (c.getCenterY() - a.getCenterY());
		Vector2f intersectionVector = new Vector2f(intersectionX, intersectionY);

		return angleFromVector(intersectionVector);
	}

	public static double angleFromVector(Vector2f vector) {
		return Math.atan2(vector.y, vector.x);
	}

	public static EnumMap<Side, Double> getSideDistances(Double radians,
			Side[] sidesToCheck) {
		EnumMap<Side, Double> distances = new EnumMap<Side, Double>(Side.class);

		for (Side side : sidesToCheck) {
			Double dist = Math.abs(side.getRadians() - radians);
			dist = wrapValue(dist, PhysicsRef.MIN_RAD, PhysicsRef.MAX_RAD);

			distances.put(side, dist);
		}

		return distances;
	}

	public static double wrapValue(double value, double min, double max) {
		if (value > max)
			return (value - max) + min;
		if (value < min)
			return max - (min - value);
		return value;
	}

	public static Vector2f vectorFromOrientationAndLength(Orientation o,
			float length) {
		switch (o) {
		case DOWN:
			return new Vector2f(0, length);
		case DOWNLEFT:
			return new Vector2f(-length / 2, length / 2);
		case DOWNRIGHT:
			return new Vector2f(length / 2, length / 2);
		case LEFT:
			return new Vector2f(-length, 0);
		case RIGHT:
			return new Vector2f(length, 0);
		case UP:
			return new Vector2f(0, -length);
		case UPLEFT:
			return new Vector2f(-length / 2, -length / 2);
		case UPRIGHT:
			return new Vector2f(length / 2, -length / 2);
		}
		return null;
	}

	public static double getArea(Rectangle rect) {
		return rect.getWidth() * rect.getHeight();
	}

	// Return a random number inside the range [low, high]
	public static int randRange(int low, int high) {
		return low + (int) (Math.random() * ((high - low) + 1));
	}

	public static double randSet(double values[]) {
		return values[(int) (Math.random() * ((values.length - 1) + 1))];
	}

	public static String randSet(String values[]) {
		return values[(int) (Math.random() * ((values.length - 1) + 1))];
	}

	public static Object randInSet(Set<?> set) {
		return new ArrayList<Object>(set).get(randRange(0, set.size() - 1));
	}

	public static Object randInArray(Object[] array) {
		return array[randRange(0, array.length - 1)];
	}

	public static Object randInList(List<?> list) {
		return list.get(randRange(0, list.size() - 1));
	}

	public static Object randInCollection(Collection<?> collection) {
		return new ArrayList<>(collection).get(randRange(0,
				collection.size() - 1));
	}

	public static Object randKeyInMap(Map<?, ?> map) {
		return randInSet(map.keySet());
	}

	public static Object randValueInMap(Map<?, ?> map) {
		return randInCollection(map.values());
	}

}
