package unnamed_platformer.game.entities;


import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.globals.GameRef.Flag;

public class Beam extends ActiveEntity {
	private static final long serialVersionUID = 6995452279880707030L;

	public Beam(EntitySetup entitySetup) {
		super(entitySetup);

		setFlag(Flag.hurtsOthers, true);
		setFlag(Flag.dissolvesOnContact, true);
	}
}
