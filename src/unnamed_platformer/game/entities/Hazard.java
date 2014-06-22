package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.interactions.Interaction_Damage;

public class Hazard extends ActiveEntity {
	private static final long serialVersionUID = -1762909115189954190L;
	
	public Hazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		interactions.add(new Interaction_Damage(this));
	}

}
