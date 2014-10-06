package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_Damaging;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.game.other.DirectionalEnums.Side;
import unnamed_platformer.globals.GameRef.Flag;

public class InvertedSpikes extends ActiveEntity
{
	public InvertedSpikes(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Inter_Damaging(this, new Side[] {
			Side.BOTTOM
		}));
		setFlag(Flag.SOLID, true);
		setFlag(Flag.ALWAYS_INTERACT, true);
	}
}
