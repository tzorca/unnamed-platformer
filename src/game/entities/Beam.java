

package game.entities;

import game.parameters.Ref.Flag;
import game.structures.FlagMap;

import java.awt.Point;

public class Beam extends ActiveEntity {

	private static final long serialVersionUID = 6995452279880707030L;

	public Beam(Point pos) {
		super("laser", pos, new FlagMap());

		setFlag(Flag.hurtsOthers, true);
		setFlag(Flag.dissolvesOnContact, true);

	}

}