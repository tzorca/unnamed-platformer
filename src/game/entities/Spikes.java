package game.entities;

import game.parameters.PhysicsRef.Side;
import game.parameters.Ref.Flag;
import game.structures.Graphic;

import java.awt.Point;
import java.io.Serializable;

public class Spikes extends Hazard implements Serializable {
	private static final long serialVersionUID = -4236233873255466151L;

	public Spikes(Graphic graphic, Point pos) {
		super(graphic, pos, new Side[] { Side.TOP });
		setFlag(Flag.solid, true);
	}

	public Spikes(Graphic graphic, Point pos, int width) {
		super(graphic, pos, width, new Side[] { Side.TOP });

		setFlag(Flag.solid, true);
	}

}
