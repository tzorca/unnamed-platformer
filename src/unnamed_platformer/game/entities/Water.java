package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.dynamics.interactions.WaterInteraction;
import unnamed_platformer.game.structures.FlColor;

public class Water extends ActiveEntity {

	private static final long serialVersionUID = 7134237510304922138L;

	public Water(EntitySetup entitySetup) {
		super(entitySetup);

		interactions.add(new WaterInteraction(this));
		zIndex = 3;
		graphic.color = new FlColor(1,1,1,0.5f);
	}
}
