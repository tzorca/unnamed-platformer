package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_Damaging;
import unnamed_platformer.game.other.EntitySetup;

public class Hazard extends ActiveEntity {
	private static final long serialVersionUID = -1762909115189954190L;
	
	public Hazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		interactions.add(new Inter_Damaging(this));
	}

}
