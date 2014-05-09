

package game.entities;

import game.parameters.Ref.Flag;
import game.structures.FlagMap;

import java.awt.Point;

public class BreakableBlock extends Entity {

	private static final long serialVersionUID = 7134237510304922138L;

	public BreakableBlock(String imageName, Point pos) {
		super(imageName, pos, new FlagMap(new Flag[] { Flag.solid,
				Flag.breakableBlock }));

	}

	public BreakableBlock(String imageName, Point pos, int width) {

		super(imageName, pos, width, new FlagMap(new Flag[] { Flag.solid,
				Flag.breakableBlock }));
	}

}
