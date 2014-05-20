package game.entities;

import game.dynamics.interactions.SpringInteraction;
import game.logic.MathHelper;
import game.parameters.PhysicsRef;
import game.structures.Graphic;

import org.newdawn.slick.geom.Vector2f;

public class SpringLike extends ActiveEntity {
	private static final long serialVersionUID = -6977486102061938504L;

	public SpringLike(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	public SpringLike(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	@Override
	protected void defaultActiveSetup() {
		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(PhysicsRef.Orientation.UP,
						PhysicsRef.DEFAULT_SPRING_STRENGTH)));
	}

}
