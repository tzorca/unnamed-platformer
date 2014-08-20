package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;

import unnamed_platformer.game.EntitySetup;

public class LiquidHazard extends Hazard {

	private static final long serialVersionUID = -1762909115189954190L;
	public LiquidHazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		zIndex = 3;
		graphic.color = new Color(1,1,1,0.75f);
	}
	

}
