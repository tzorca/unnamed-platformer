package game.entities;

import game.parameters.Ref.Flag;
import game.structures.FlagMap;
import game.structures.Graphic;

import java.awt.Point;

public class SolidBlock extends Entity {
	private static final long serialVersionUID = -6385913941765259912L;

	public SolidBlock(Graphic graphic, Point pos) {
		super(graphic, pos, new FlagMap(new Flag[] { Flag.solid }));
	}

	public SolidBlock(Graphic graphic, Point pos, int width) {
		super(graphic, pos, width, new FlagMap(new Flag[] { Flag.solid }));
	}

}
