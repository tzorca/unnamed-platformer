package unnamed_platformer.game.entities;

import java.io.Serializable;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.dynamics.interactions.HurtOnInteract;
import unnamed_platformer.game.parameters.PhysicsRef.Side;
import unnamed_platformer.game.parameters.Ref.Flag;

public class Spikes extends ActiveEntity implements Serializable {
	private static final long serialVersionUID = -4236233873255466151L;

	public Spikes(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions
				.add(new HurtOnInteract(this, new Side[] { Side.TOP }));
		setFlag(Flag.solid, true);
	}
}
