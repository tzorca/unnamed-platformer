package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_InstantDeath;
import unnamed_platformer.game.other.EntitySetup;

public class DeadlyHazard extends ActiveEntity
{
	public DeadlyHazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		interactions.add(new Inter_InstantDeath(this));
	}

}
