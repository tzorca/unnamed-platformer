package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.GUIManager.ScreenType;

public class Inter_LevelExit extends Interaction
{
	int relativeDestination;

	public Inter_LevelExit(Entity source, int relativeDestination) {
		super(source);
		this.relativeDestination = relativeDestination;
	}

	@Override
	public InteractionResult performInteraction(Entity target) {
		// TODO: Show current level in HUD
		// TODO: Create GameFinished screen and ask if user wants to restart
		World.setLevelByIndex(World.getCurrentLevelIndex()
				+ relativeDestination);
		World.setPlaying(false);
		GUIManager.changeScreen(ScreenType.Transition);

		return InteractionResult.SKIP_PHYSICS;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
