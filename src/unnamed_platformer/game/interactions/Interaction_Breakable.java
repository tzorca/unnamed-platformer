package unnamed_platformer.game.interactions;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;

public class Interaction_Breakable extends Interaction {
	private static final long serialVersionUID = 3545111742560282316L;

	public Interaction_Breakable(Entity source) {
		super(source);
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.hurtsOthers);
	}

	@Override
	protected InteractionResult performInteraction(Entity target) {
		source.setFlag(Flag.outOfPlay, true);
		if (target.isFlagSet(Flag.dissolvesOnContact))
			target.setFlag(Flag.outOfPlay,  true);
		return InteractionResult.NO_RESULT;
	}

}
