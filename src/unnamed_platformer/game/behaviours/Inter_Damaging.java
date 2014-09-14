package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.game.other.DirectionalEnums.Side;
import unnamed_platformer.globals.GameRef.Flag;

// TODO: Add health system
// TODO: Implement temporary invincibility after nonlethal hit
// TODO: Implement small waiting period after death but before restarting level
public class Inter_Damaging extends Interaction {

	public Inter_Damaging(Entity source) {
		super(source);
	}

	public Inter_Damaging(Entity source, Side[] sides) {
		super(source, sides, true);

	}

	@Override
	public boolean performInteraction(Entity target) {
		((PlatformPlayer) target).damage();
		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
