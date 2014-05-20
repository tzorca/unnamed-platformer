package game.entities;

import game.dynamics.interactions.HurtOnInteract;
import game.structures.Graphic;

import org.newdawn.slick.geom.Vector2f;

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
