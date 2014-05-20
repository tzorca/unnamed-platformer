package game.entities;

import game.parameters.Ref.Flag;
import game.structures.Graphic;

import org.newdawn.slick.geom.Vector2f;

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
