package game.entities;

import game.parameters.Ref.Flag;
import game.structures.Graphic;

import java.awt.Point;
import java.util.EnumSet;

public class SolidBlock extends Entity {
	private static final long serialVersionUID = -6385913941765259912L;

	public SolidBlock(Graphic graphic, Point pos) {
		super(graphic, pos, EnumSet.noneOf(Flag.class));

		setFlag(Flag.solid, true);
	}

	public SolidBlock(Graphic graphic, Point pos, int width) {
		super(graphic, pos, width, EnumSet.noneOf(Flag.class));

		setFlag(Flag.solid, true);
	}

}
