package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;

public class Inter_LevelExit extends Interaction {
	int relativeDestination;

	public Inter_LevelExit(Entity source, int relativeDestination) {
		super(source);
		this.relativeDestination = relativeDestination;
	}

	@Override
	public InteractionResult performInteraction(Entity target) {
		// TODO: Show level end animation (probably as a Screen)
		// TODO: Show current level in HUD
		// TODO: Create GameFinished screen and ask if user wants to restart
		World.setLevelByIndex(World.getCurrentLevelIndex()
				+ relativeDestination);

		return InteractionResult.SKIP_PHYSICS;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
