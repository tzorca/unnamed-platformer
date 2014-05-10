package game.dynamics.interactions;

import game.entities.Entity;
import game.parameters.Ref.Flag;
import app.LevelManager;

public class LevelExitInteraction extends Interaction {
	private static final long serialVersionUID = -4245200837591120807L;
	int relativeDestination;

	public LevelExitInteraction(Entity source, int relativeDestination) {
		super(source);
		this.relativeDestination = relativeDestination;
	}

	@Override
	public void interactWith(Entity target) {
		if (target.checkFlag(Flag.player)) {
			LevelManager.changeLevel(LevelManager.getLevelNumber()
					+ relativeDestination);
		}

	}

}