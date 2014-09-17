package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.behaviours.Inter_Spring;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.game.other.DirectionalEnums.Orientation;
import unnamed_platformer.globals.GameRef;

public class SpringLike extends ActiveEntity {

	public SpringLike(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Inter_Spring(MathHelper
				.vectorFromOrientationAndLength(Orientation.UP,
						GameRef.DEFAULT_SPRING_STRENGTH)));
	}

}
