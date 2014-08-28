package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef;

public class Inter_SlowRegion extends Inter_VelocityMultiplier {
	private static final long serialVersionUID = -6008864799350154417L;

	public Inter_SlowRegion(Entity source) {
		super(source, GameRef.DEFAULT_WATER_SPEED_FACTOR);
	}

}
