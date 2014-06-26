package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.interactions.Interaction_Spring;
import unnamed_platformer.globals.GameRef;
import unnamed_platformer.globals.PhysicsRef;

// TODO: Fix spring physics (way too powerful)
public class SpringLike extends ActiveEntity {
	private static final long serialVersionUID = -6977486102061938504L;

	public SpringLike(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Interaction_Spring(this, MathHelper
				.vectorFromOrientationAndLength(PhysicsRef.Orientation.UP,
						GameRef.DEFAULT_SPRING_STRENGTH)));
	}

}
