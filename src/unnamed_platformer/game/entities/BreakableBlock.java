package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.parameters.Ref.Flag;

public class BreakableBlock extends Entity {
	private static final long serialVersionUID = 7134237510304922138L;

	public BreakableBlock(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.solid, true);
		setFlag(Flag.breakableBlock, true);
	}
}
