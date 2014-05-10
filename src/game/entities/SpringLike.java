package game.entities;

import game.dynamics.interactions.SpringInteraction;
import game.logic.MathHelper;
import game.parameters.PhysicsRef;
import game.parameters.PhysicsRef.Orientation;
import game.structures.Graphic;

import java.awt.Point;

public class SpringLike extends ActiveEntity {
	private static final long serialVersionUID = -6977486102061938504L;

	public SpringLike(Graphic graphic, Point pos, Orientation orientation,
			Float strength) {
		super(graphic, pos);

		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(orientation, strength)));
	}

	public SpringLike(Graphic graphic, Point pos) {
		super(graphic, pos);

		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(PhysicsRef.Orientation.UP,
						PhysicsRef.DEFAULT_SPRING_STRENGTH)));
	}

}
