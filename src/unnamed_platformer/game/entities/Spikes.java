package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_Damaging;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.game.physics.DirectionalEnums.Side;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Spikes extends ActiveEntity {
	public Spikes(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions
				.add(new Inter_Damaging(this, new Side[] { Side.TOP }));
		setFlag(Flag.SOLID, true);
		setFlag(Flag.ALWAYS_INTERACT, true);
	}
}
