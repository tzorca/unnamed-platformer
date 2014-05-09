package game.dynamics.interactions;

import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.Ref.Flag;

public class NegativeHazardReaction extends Interaction {
	private static final long serialVersionUID = 3886834248152875541L;

	public NegativeHazardReaction(Entity source) {
		super(source);
	}

	@Override
	public void interactWith(Entity target) {
		if (target.checkFlag(Flag.hurtsYou)) {
			((ActiveEntity) source).returnToStart();
		}

	}

}
