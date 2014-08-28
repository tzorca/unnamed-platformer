package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.globals.GameRef.Flag;

public class SolidBlock extends Entity {
	private static final long serialVersionUID = -6385913941765259912L;

	public SolidBlock(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.SOLID, true);
	}
}
