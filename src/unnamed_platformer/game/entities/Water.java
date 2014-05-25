package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.dynamics.interactions.WaterInteraction;
import unnamed_platformer.game.structures.FlColor;
import unnamed_platformer.game.structures.Graphic;

public class Water extends ActiveEntity {

	private static final long serialVersionUID = 7134237510304922138L;

	public Water(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	public Water(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	@Override
	protected void defaultActiveSetup() {
		interactions.add(new WaterInteraction(this));
		zIndex = 3;
		graphic.color = new FlColor(1,1,1,0.5f);
	}
}
