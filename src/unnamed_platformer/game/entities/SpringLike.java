package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.behaviours.Inter_Spring;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.game.physics.DirectionalEnums.Orientation;
import unnamed_platformer.globals.GameGlobals;

public class SpringLike extends ActiveEntity {

	public SpringLike(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Inter_Spring(MathHelper
				.vectorFromOrientationAndLength(Orientation.UP,
						GameGlobals.SPRING_STRENGTH)));
	}

}
