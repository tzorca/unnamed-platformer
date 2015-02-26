package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;

import unnamed_platformer.game.behaviours.Inter_VelocityMultiplier;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameGlobals;

public class SpeedZone extends ActiveEntity
{
	public SpeedZone(EntitySetup entitySetup) {
		super(entitySetup);

		interactions.add(new Inter_VelocityMultiplier(
				GameGlobals.FAST_REGION_SPEED_MODIFIER));
		zIndex = 3;
		graphic.color = new Color(1, 1, 1, 0.5f);
	}
}
