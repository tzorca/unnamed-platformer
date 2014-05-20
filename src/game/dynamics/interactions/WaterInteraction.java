package game.dynamics.interactions;

import game.entities.Entity;
import game.parameters.PhysicsRef;

public class WaterInteraction extends VelocityMultiplierInteraction {
	private static final long serialVersionUID = -6008864799350154417L;

	public WaterInteraction(Entity source) {
		super(source, PhysicsRef.WATER_SPEED_FACTOR);
	}

}
