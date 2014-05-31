package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.structures.Graphic;

public class BreakableBlock extends Entity {

	private static final long serialVersionUID = 7134237510304922138L;

	public BreakableBlock(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	public BreakableBlock(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	@Override
	protected void defaultSetup() {
		setFlag(Flag.solid, true);
		setFlag(Flag.breakableBlock, true);
	}

}
