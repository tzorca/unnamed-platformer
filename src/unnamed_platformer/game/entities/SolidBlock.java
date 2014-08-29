package unnamed_platformer.game.entities;

import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameRef.Flag;

public class SolidBlock extends Entity {
	public SolidBlock(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.SOLID, true);
	}
}
