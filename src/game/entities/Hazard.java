package game.entities;

import game.parameters.Ref.Flag;
import game.structures.FlagMap;
import game.structures.Graphic;

import java.awt.Point;

public class Hazard extends Entity {
	private static final long serialVersionUID = -1762909115189954190L;

	public Hazard(Graphic graphic, Point pos) {
		super(graphic, pos, new FlagMap(new Flag[] { Flag.hurtsYou }));
	}

	public Hazard(Graphic graphic, Point pos, int width) {
		super(graphic, pos, width, new FlagMap(new Flag[] { Flag.hurtsYou }));
	}
}
