package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;

public class Inter_Breakable extends Interaction {
	private static final long serialVersionUID = 3545111742560282316L;

	public Inter_Breakable(Entity source) {
		super(source);
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.HURTS_OTHERS);
	}

	@Override
	protected InteractionResult performInteraction(Entity target) {
		source.setFlag(Flag.OUT_OF_PLAY, true);
		if (target.isFlagSet(Flag.DISSOLVES_ON_CONTACT))
			target.setFlag(Flag.OUT_OF_PLAY,  true);
		return InteractionResult.NO_RESULT;
	}

}
