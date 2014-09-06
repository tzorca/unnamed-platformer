package unnamed_platformer.structures;


public class DirectionalEnums
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


}
