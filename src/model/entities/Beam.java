// Tim Zorca
// CPSC 3520
package model.entities;

import java.awt.Point;

import model.Ref.Flag;
import model.structures.BehaviourList;
import model.structures.FlagMap;

public class Beam extends ActiveEntity {

	private static final long serialVersionUID = 6995452279880707030L;

	public Beam(Point pos) {
		super("laser", pos, new FlagMap(), new BehaviourList());

		setFlag(Flag.hurtsOthers, true);
		setFlag(Flag.dissolvesOnContact, true);

	}

}
