package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;

public class Inter_DissolveOnContact extends Interaction
{

	public Inter_DissolveOnContact() {
		super();
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.SOLID);
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		source.setFlag(Flag.OUT_OF_PLAY, true);
		return true;
	}

}
