package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_Damaging;
import unnamed_platformer.game.other.EntitySetup;

public class Hazard extends ActiveEntity {
	
	public Hazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		interactions.add(new Inter_Damaging(this));
	}

}
