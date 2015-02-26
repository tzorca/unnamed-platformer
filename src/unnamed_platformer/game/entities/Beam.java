package unnamed_platformer.game.entities;


import unnamed_platformer.game.behaviours.Inter_DissolveOnContact;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Beam extends ActiveEntity {

	public Beam(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.HURTS_OTHERS, true);		
		interactions.add(new Inter_DissolveOnContact());
	}
}
