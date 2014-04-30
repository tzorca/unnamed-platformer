// Tim Zorca
// CPSC 3520
package model.entities;

import java.awt.Point;

import model.Ref.Flag;
import model.structures.FlagMap;

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
