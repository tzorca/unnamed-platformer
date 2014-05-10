package game.entities;

import game.parameters.Ref.Flag;
import game.structures.FlagMap;
import game.structures.Graphic;

import java.awt.Point;

public class BreakableBlock extends Entity {

	private static final long serialVersionUID = 7134237510304922138L;

	public BreakableBlock(Graphic graphic, Point pos) {
		super(graphic, pos, new FlagMap(new Flag[] { Flag.solid,
				Flag.breakableBlock }));

	}

	public BreakableBlock(Graphic graphic, Point pos, int width) {

		super(graphic, pos, width, new FlagMap(new Flag[] { Flag.solid,
				Flag.breakableBlock }));
	}

}
