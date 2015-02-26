package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;

import unnamed_platformer.game.editor.EntitySetup;

public class LiquidHazard extends DeadlyHazard {

	public LiquidHazard(EntitySetup entitySetup) {
		super(entitySetup);
		
		zIndex = 3;
		graphic.color = new Color(1,1,1,0.85f);
	}
	

}
