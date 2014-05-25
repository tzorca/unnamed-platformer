package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.dynamics.interactions.LevelExitInteraction;
import unnamed_platformer.game.structures.Graphic;

public class Goal extends ActiveEntity {
	private static final long serialVersionUID = -2029837091925845639L;

	public Goal(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}
	
	public Goal(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}
	
	@Override
	protected void defaultActiveSetup() {
		this.interactions.add(new LevelExitInteraction(this, 1));
	}

}
