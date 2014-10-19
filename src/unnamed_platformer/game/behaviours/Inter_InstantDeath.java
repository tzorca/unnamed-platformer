package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.globals.GameRef.Flag;

public class Inter_InstantDeath extends Interaction
{
	public Inter_InstantDeath() {
		super();
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		PlatformPlayer player = (PlatformPlayer) target;
		player.death();
		return true;
	}

}
