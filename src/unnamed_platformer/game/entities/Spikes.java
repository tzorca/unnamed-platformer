package unnamed_platformer.game.entities;

import java.io.Serializable;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.interactions.Interaction_Damaging;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.PhysicsRef.Side;

public class Spikes extends ActiveEntity implements Serializable {
	private static final long serialVersionUID = -4236233873255466151L;

	public Spikes(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions
				.add(new Interaction_Damaging(this, new Side[] { Side.TOP }));
		setFlag(Flag.SOLID, true);
	}
}
