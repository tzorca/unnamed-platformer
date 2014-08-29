package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_LevelExit;
import unnamed_platformer.game.other.EntitySetup;

public class Goal extends ActiveEntity {
	private static final long serialVersionUID = -2029837091925845639L;

	public Goal(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Inter_LevelExit(this, 1));
	}
}
