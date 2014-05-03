package model.entities;

import java.awt.Point;

import model.parameters.Ref.Flag;
import model.structures.FlagMap;

public class Hazard extends Entity {
	private static final long serialVersionUID = -1762909115189954190L;

	public Hazard(String texName, Point pos) {
		super(texName, pos, new FlagMap(new Flag[] { Flag.hurtsYou }));
	}

	public Hazard(String texName, Point pos, int width) {
		super(texName, pos, width, new FlagMap(new Flag[] { Flag.hurtsYou }));
	}
}
