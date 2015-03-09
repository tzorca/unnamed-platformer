package unnamed_platformer.game.entities;

import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.globals.GameGlobals.Flag;

public class BackgroundBlock extends Entity {
	public BackgroundBlock(EntitySetup entitySetup) {
		super(entitySetup);
		
		setFlag(Flag.SOLID, false);
		zIndex = -1;
	}
}
