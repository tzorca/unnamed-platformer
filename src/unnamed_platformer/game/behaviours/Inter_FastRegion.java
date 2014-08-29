package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef;

public class Inter_FastRegion extends Inter_VelocityMultiplier {

	public Inter_FastRegion(Entity source) {
		super(source, GameRef.DEFAULT_FAST_REGION_SPEED_FACTOR);
	}

}
