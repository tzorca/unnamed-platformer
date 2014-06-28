package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.interactions.Interaction_Breakable;
import unnamed_platformer.globals.GameRef.Flag;

public class BreakableBlock extends ActiveEntity {
	private static final long serialVersionUID = 7134237510304922138L;

	public BreakableBlock(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.solid, true);
		this.interactions.add(new Interaction_Breakable(this));
	}
}
