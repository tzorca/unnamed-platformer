package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Inter_HealthModifier extends Interaction {
	private int healthDelta;
	
	public Inter_HealthModifier(int healthDelta) {
		super();
		this.healthDelta = healthDelta;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		PlatformPlayer player = (PlatformPlayer)target;
		
		source.setFlag(Flag.OUT_OF_PLAY, true);
		player.addHealth(healthDelta);

		return true;
	}

}
