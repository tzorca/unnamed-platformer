package unnamed_platformer.game.interactions;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.Ref.Flag;

public class Interaction_LevelExit extends Interaction {
	private static final long serialVersionUID = -4245200837591120807L;
	int relativeDestination;

	public Interaction_LevelExit(Entity source, int relativeDestination) {
		super(source);
		this.relativeDestination = relativeDestination;
	}

	@Override
	public void duringInteraction(Entity target) {
		// TODO: Show level end animation (probably as a Screen)
		// TODO: Show current level in HUD
		// TODO: Create GameFinished screen and ask if user wants to restart
		GameManager.changeLevelToIndex(GameManager.getCurrentLevelNumber()
				+ relativeDestination);
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

}
