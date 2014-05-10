package game.entities;

import game.dynamics.interactions.LevelExitInteraction;
import game.structures.Graphic;

import java.awt.Point;

public class Goal extends ActiveEntity {
	private static final long serialVersionUID = -2029837091925845639L;

	public Goal(Graphic graphic, Point pos) {
		super(graphic, pos);
		this.interactions.add(new LevelExitInteraction(this, 1));
	}
}
