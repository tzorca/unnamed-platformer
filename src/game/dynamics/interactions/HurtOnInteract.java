package game.dynamics.interactions;

import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.Ref.Flag;

public class HurtOnInteract extends Interaction {
	private static final long serialVersionUID = 3886834248152875541L;

	public HurtOnInteract(Entity source) {
		super(source);
	}

	@Override
	public void interactWith(Entity target) {
		if (target.checkFlag(Flag.player)) {
			((ActiveEntity) target).returnToStart();
		}

	}

}
