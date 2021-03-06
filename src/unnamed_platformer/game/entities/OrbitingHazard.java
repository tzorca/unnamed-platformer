package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.behaviours.Ctrl_PathMovement;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.globals.GameGlobals;
public class OrbitingHazard extends Hazard {
	private Ctrl_PathMovement pathMovement;

	public OrbitingHazard(EntitySetup entitySetup) {
		super(entitySetup);
		addPathMovement();
	}

	private void addPathMovement() {
		pathMovement = new Ctrl_PathMovement(this, this.startPos,
				MathHelper.createCirclePath(64), GameGlobals.ORBITING_HAZARD_SPEED);
		pathMovement.setLoop(true);
		this.getPhysics().addControlMechanism(pathMovement);
	}


}
