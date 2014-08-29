package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_Breakable;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameRef.Flag;

public class BreakableBlock extends ActiveEntity {

	public BreakableBlock(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.SOLID, true);
		this.interactions.add(new Inter_Breakable(this));
	}
}
