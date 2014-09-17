package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;

import unnamed_platformer.game.behaviours.Inter_FastRegion;
import unnamed_platformer.game.other.EntitySetup;

public class SpeedZone extends ActiveEntity {

	public SpeedZone(EntitySetup entitySetup) {
		super(entitySetup);

		interactions.add(new Inter_FastRegion());
		zIndex = 3;
		graphic.color = new Color(1,1,1,0.5f);
	}
}
