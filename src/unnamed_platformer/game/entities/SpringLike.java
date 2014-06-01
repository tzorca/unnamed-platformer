package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.dynamics.interactions.SpringInteraction;
import unnamed_platformer.game.logic.MathHelper;
import unnamed_platformer.game.parameters.PhysicsRef;

// TODO: Fix spring physics (way too powerful)
public class SpringLike extends ActiveEntity {
	private static final long serialVersionUID = -6977486102061938504L;

	public SpringLike(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new SpringInteraction(this, MathHelper
				.vectorFromOrientationAndLength(PhysicsRef.Orientation.UP,
						PhysicsRef.DEFAULT_SPRING_STRENGTH)));
	}

}
