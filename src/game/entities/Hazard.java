package game.entities;

import game.dynamics.interactions.HurtOnInteract;
import game.parameters.PhysicsRef.Side;
import game.structures.FlagMap;
import game.structures.Graphic;

import java.awt.Point;

public class Hazard extends ActiveEntity {
	private static final long serialVersionUID = -1762909115189954190L;

	public Hazard(Graphic graphic, Point pos) {
		super(graphic, pos, new FlagMap());

		interactions.add(new HurtOnInteract(this));
	}

	public Hazard(Graphic graphic, Point pos, int width) {
		super(graphic, pos, width, new FlagMap());

		interactions.add(new HurtOnInteract(this));
	}

	public Hazard(Graphic graphic, Point pos, int width, Side[] activeSides) {
		super(graphic, pos, width, new FlagMap());

		interactions.add(new HurtOnInteract(this, activeSides));
	}

	public Hazard(Graphic graphic, Point pos, Side[] activeSides) {
		super(graphic, pos, new FlagMap());

		interactions.add(new HurtOnInteract(this, activeSides));
	}
}
