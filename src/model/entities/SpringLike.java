package model.entities;

import java.awt.Point;

import model.Ref.Orientation;
import model.dynamics.interactions.SpringInteraction;
import model.logic.MathHelper;

public class SpringLike extends ActiveEntity {
	private static final long serialVersionUID = -6977486102061938504L;

	public SpringLike(String texName, Point pos, Orientation orientation,
			Float strength) {
		super(texName, pos);

		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(orientation, strength)));

	}

}
