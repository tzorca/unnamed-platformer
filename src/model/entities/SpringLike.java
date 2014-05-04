package model.entities;

import java.awt.Point;

import model.dynamics.interactions.SpringInteraction;
import model.logic.MathHelper;
import model.parameters.PhysicsRef;
import model.parameters.PhysicsRef.Orientation;

public class SpringLike extends ActiveEntity {
	private static final long serialVersionUID = -6977486102061938504L;

	public SpringLike(String texName, Point pos, Orientation orientation,
			Float strength) {
		super(texName, pos);

		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(orientation, strength)));
	}

	public SpringLike(String texName, Point pos) {
		super(texName, pos);

		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(PhysicsRef.Orientation.UP,
						PhysicsRef.DEFAULT_SPRING_STRENGTH)));
	}

}
