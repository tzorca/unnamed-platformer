package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.parameters.Ref.Flag;

public class SolidBlock extends Entity {
	private static final long serialVersionUID = -6385913941765259912L;

	public SolidBlock(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.solid, true);
	}
}
