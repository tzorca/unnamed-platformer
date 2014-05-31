package unnamed_platformer.game.dynamics.interactions;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.parameters.PhysicsRef.Side;
import unnamed_platformer.game.parameters.Ref.Flag;

// TODO: Add health system
// TODO: Implement temporary invincibility after nonlethal hit
// TODO: Implement small waiting period after death but before restarting level
public class HurtOnInteract extends Interaction {
	private static final long serialVersionUID = 3886834248152875541L;

	public HurtOnInteract(Entity source) {
		super(source);
	}

	public HurtOnInteract(Entity source, Side[] sides) {
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
