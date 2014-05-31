package unnamed_platformer.game.dynamics.interactions;

import unnamed_platformer.app.LevelManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.parameters.Ref.Flag;

public class LevelExitInteraction extends Interaction {
	private static final long serialVersionUID = -4245200837591120807L;
	int relativeDestination;

	public LevelExitInteraction(Entity source, int relativeDestination) {
		super(source);
		this.relativeDestination = relativeDestination;
	}

	@Override
	public void duringInteraction(Entity target) {
		// TODO: Show level end animation (probably as a Screen)
		// TODO: Show current level in HUD
		// TODO: Create GameFinished screen and ask if user wants to restart
		LevelManager.changeLevel(LevelManager.getLevelNumber()
				+ relativeDestination);
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

}
