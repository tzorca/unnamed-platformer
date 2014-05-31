package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.structures.Graphic;

public class SolidBlock extends Entity {
	private static final long serialVersionUID = -6385913941765259912L;

	public SolidBlock(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	public SolidBlock(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	@Override
	protected void defaultSetup() {

		setFlag(Flag.solid, true);
	}

}
