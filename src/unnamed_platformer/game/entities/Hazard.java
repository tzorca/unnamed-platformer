package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.dynamics.interactions.HurtOnInteract;
import unnamed_platformer.game.structures.Graphic;

public class Hazard extends ActiveEntity {
	private static final long serialVersionUID = -1762909115189954190L;
	
	public Hazard(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}
	
	public Hazard(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}


	@Override
	protected void defaultActiveSetup() {
		interactions.add(new HurtOnInteract(this));
	}
}
