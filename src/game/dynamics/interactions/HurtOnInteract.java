package game.dynamics.interactions;

import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.PhysicsRef.Side;
import game.parameters.Ref.Flag;

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
