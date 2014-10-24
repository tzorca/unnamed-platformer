package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.behaviours.Ctrl_PathMovement;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameRef;
import unnamed_platformer.globals.GameRef.Flag;

public class MotionBoxUpDown extends ActiveEntity
{

	private Ctrl_PathMovement pathMovement;

	public MotionBoxUpDown(EntitySetup entitySetup) {
		super(entitySetup);
		setFlag(Flag.SOLID, true);
		addPathMovement();
	}

	private void addPathMovement() {
		pathMovement = new Ctrl_PathMovement(this, this.startPos,
				MathHelper.createUpDownPath(GameRef.DEFAULT_MOTION_BOX_DIST),
				GameRef.DEFAULT_MOTION_BOX_SPEED);
		pathMovement.setLoop(true);
		this.getPhysics().addControlMechanism(pathMovement);
	}

}
