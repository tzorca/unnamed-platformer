package unnamed_platformer.game.entities;

import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.dynamics.interactions.HurtOnInteract;
import unnamed_platformer.game.parameters.PhysicsRef.Side;
import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.structures.Graphic;

public class Spikes extends ActiveEntity implements Serializable {
	private static final long serialVersionUID = -4236233873255466151L;

	public Spikes(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	public Spikes(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	@Override
	protected void defaultActiveSetup() {
		this.interactions
				.add(new HurtOnInteract(this, new Side[] { Side.TOP }));
		setFlag(Flag.solid, true);
	}

}
