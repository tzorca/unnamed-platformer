package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_HealthModifier;
import unnamed_platformer.game.editor.EntitySetup;

public class HealthPickup extends ActiveEntity {

	public HealthPickup(EntitySetup entitySetup) {
		super(entitySetup);

		interactions.add(new Inter_HealthModifier(1));
	}

}
