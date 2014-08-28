package unnamed_platformer.game.entities;

import java.io.Serializable;

import unnamed_platformer.app.MathHelper.Side;
import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.behaviours.Inter_Damaging;
import unnamed_platformer.globals.GameRef.Flag;

public class Spikes extends ActiveEntity implements Serializable {
	private static final long serialVersionUID = -4236233873255466151L;

	public Spikes(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions
				.add(new Inter_Damaging(this, new Side[] { Side.TOP }));
		setFlag(Flag.SOLID, true);
	}
}
