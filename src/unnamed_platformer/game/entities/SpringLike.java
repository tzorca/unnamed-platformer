package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.behaviours.Inter_Spring;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameRef;
import unnamed_platformer.structures.DirectionalEnums.Orientation;

public class SpringLike extends ActiveEntity {

	public SpringLike(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Inter_Spring(this, MathHelper
				.vectorFromOrientationAndLength(Orientation.UP,
						GameRef.DEFAULT_SPRING_STRENGTH)));
	}

}
