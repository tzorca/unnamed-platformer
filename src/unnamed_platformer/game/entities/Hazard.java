package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.dynamics.interactions.HurtOnInteract;

public class Hazard extends ActiveEntity {
	private static final long serialVersionUID = -1762909115189954190L;
	
	public Hazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		interactions.add(new HurtOnInteract(this));
	}

}
