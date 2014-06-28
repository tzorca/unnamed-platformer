package unnamed_platformer.game.interactions;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;
import unnamed_platformer.globals.PhysicsRef.Side;

// TODO: Add health system
// TODO: Implement temporary invincibility after nonlethal hit
// TODO: Implement small waiting period after death but before restarting level
public class Interaction_Damaging extends Interaction {
	private static final long serialVersionUID = 3886834248152875541L;

	public Interaction_Damaging(Entity source) {
		super(source);
	}

	public Interaction_Damaging(Entity source, Side[] sides) {
		super(source, sides, true);

	}

	@Override
	public InteractionResult performInteraction(Entity target) {
		((ActiveEntity) target).returnToStart();
		return InteractionResult.SKIP_PHYSICS;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

}
