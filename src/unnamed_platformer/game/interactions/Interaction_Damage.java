package unnamed_platformer.game.interactions;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.PhysicsRef.Side;
import unnamed_platformer.globals.Ref.Flag;

// TODO: Add health system
// TODO: Implement temporary invincibility after nonlethal hit
// TODO: Implement small waiting period after death but before restarting level
public class Interaction_Damage extends Interaction {
	private static final long serialVersionUID = 3886834248152875541L;

	public Interaction_Damage(Entity source) {
		super(source);
	}

	public Interaction_Damage(Entity source, Side[] sides) {
		super(source, sides, true);

	}

	@Override
	public void duringInteraction(Entity target) {
		((ActiveEntity) target).returnToStart();
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

}
