package game.entities;

import game.dynamics.interactions.LevelExitInteraction;

import java.awt.Point;

public class Goal extends ActiveEntity {
	private static final long serialVersionUID = -2029837091925845639L;

	public Goal(String texName, Point pos) {
		super(texName, pos);
		this.interactions.add(new LevelExitInteraction(this, 1));
	}
}
