package unnamed_platformer.game.interactions;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef;

public class Interaction_Water extends Interaction_VelocityMultiplier {
	private static final long serialVersionUID = -6008864799350154417L;

	public Interaction_Water(Entity source) {
		super(source, GameRef.DEFAULT_WATER_SPEED_FACTOR);
	}

}
