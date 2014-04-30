// Tim Zorca
// CPSC 3520
package model.entities;

import java.awt.Point;

import model.Ref.Flag;
import model.structures.FlagMap;

public class SolidBlock extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6385913941765259912L;

	public SolidBlock(String imageName, Point pos) {
		super(imageName, pos, new FlagMap(new Flag[] { Flag.solid }));
	}

	public SolidBlock(String imageName, Point pos, int width) {
		super(imageName, pos, width, new FlagMap(new Flag[] { Flag.solid }));
	}

}
