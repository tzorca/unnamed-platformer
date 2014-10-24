package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;

public class Inter_Breakable extends Interaction
{

	public Inter_Breakable() {
		super();
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.HURTS_OTHERS);
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		source.setFlag(Flag.INACTIVE_UNTIL_PLAYER_DEATH, true);
		source.setFlag(Flag.INVISIBLE, true);
		return true;
	}

}
