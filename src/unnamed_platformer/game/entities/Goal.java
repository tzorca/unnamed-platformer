package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.dynamics.interactions.LevelExitInteraction;

public class Goal extends ActiveEntity {
	private static final long serialVersionUID = -2029837091925845639L;

	public Goal(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new LevelExitInteraction(this, 1));
	}
}
