package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.interactions.Interaction_Water;

public class Water extends ActiveEntity {

	private static final long serialVersionUID = 7134237510304922138L;

	public Water(EntitySetup entitySetup) {
		super(entitySetup);

		interactions.add(new Interaction_Water(this));
		zIndex = 3;
		graphic.color = new Color(1,1,1,0.5f);
	}
}
