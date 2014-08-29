package unnamed_platformer.game.behaviours;

import unnamed_platformer.app.MathHelper.Side;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;

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
	public InteractionResult performInteraction(Entity target) {
		((ActiveEntity) target).returnToStart();
		return InteractionResult.SKIP_PHYSICS;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
