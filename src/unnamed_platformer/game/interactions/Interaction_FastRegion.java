package unnamed_platformer.game.interactions;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef;

public class Interaction_FastRegion extends Interaction_VelocityMultiplier {
	private static final long serialVersionUID = 1926261542858628270L;

	public Interaction_FastRegion(Entity source) {
		super(source, GameRef.DEFAULT_FAST_REGION_SPEED_FACTOR);
	}

}
