package game.entities;


import game.parameters.Ref.Flag;
import game.structures.Graphic;

import org.newdawn.slick.geom.Vector2f;

public class Beam extends ActiveEntity {
	private static final long serialVersionUID = 6995452279880707030L;

	public Beam(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	public Beam(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	@Override
	protected void defaultActiveSetup() {

		setFlag(Flag.hurtsOthers, true);
		setFlag(Flag.dissolvesOnContact, true);
	}

}
