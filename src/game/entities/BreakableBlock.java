package game.entities;

import game.parameters.Ref.Flag;
import game.structures.Graphic;

import java.awt.Point;
import java.util.EnumSet;

public class BreakableBlock extends Entity {

	private static final long serialVersionUID = 7134237510304922138L;

	public BreakableBlock(Graphic graphic, Point pos) {
		super(graphic, pos, EnumSet.noneOf(Flag.class));

		setFlag(Flag.solid, true);
		setFlag(Flag.breakableBlock, true);

	}

	public BreakableBlock(Graphic graphic, Point pos, int width) {

		super(graphic, pos, width, EnumSet.noneOf(Flag.class));

		setFlag(Flag.solid, true);
		setFlag(Flag.breakableBlock, true);
	}

}
