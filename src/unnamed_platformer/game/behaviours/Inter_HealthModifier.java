package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;

public class Inter_HealthModifier extends Interaction {
	private int healthDelta;
	
	public Inter_HealthModifier(Entity source, int healthDelta) {
		super(source);
		this.healthDelta = healthDelta;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

	@Override
	protected InteractionResult performInteraction(Entity target) {
		PlatformPlayer player = (PlatformPlayer)target;
		
		source.setFlag(Flag.OUT_OF_PLAY, true);
		player.addHealth(healthDelta);
		
		return InteractionResult.BLANK_RESULT;
	}

}
