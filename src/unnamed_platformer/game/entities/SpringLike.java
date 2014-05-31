package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.dynamics.interactions.SpringInteraction;
import unnamed_platformer.game.logic.MathHelper;
import unnamed_platformer.game.parameters.PhysicsRef;
import unnamed_platformer.game.structures.Graphic;

// TODO: Fix spring physics (way too powerful)
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
