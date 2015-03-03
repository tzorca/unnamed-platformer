package unnamed_platformer.game.entities;

import unnamed_platformer.game.behaviours.Inter_Bounce;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.globals.GameGlobals;

public class Bumper extends ActiveEntity
{

	public Bumper(EntitySetup entitySetup) {
		super(entitySetup);

		this.interactions.add(new Inter_Bounce(GameGlobals.BUMPER_STRENGTH,
				GameGlobals.BUMPER_CONTROL_LOCK_TIME));
	}

}
