package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.game.other.DirectionalEnums.Side;
import unnamed_platformer.globals.GameRef.Flag;

public class Inter_Damaging extends Interaction {

	public Inter_Damaging() {
		super();
	}

	public Inter_Damaging(Entity source, Side[] sides) {
		super(source, sides, true);

	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		((PlatformPlayer) target).damage();
		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
