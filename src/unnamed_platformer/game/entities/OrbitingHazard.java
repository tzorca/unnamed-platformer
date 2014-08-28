package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.behaviours.Ctrl_PathMovement;
import unnamed_platformer.globals.GameRef;

public class OrbitingHazard extends Hazard {
	private static final long serialVersionUID = -5577090743934227764L;
	
	private Ctrl_PathMovement pathMovement;

	public OrbitingHazard(EntitySetup entitySetup) {
		super(entitySetup);
		addPathMovement();
	}

	private void addPathMovement() {
		pathMovement = new Ctrl_PathMovement(this, this.startPos,
				MathHelper.createCirclePath(64), GameRef.DEFAULT_ORBIT_HAZARD_SPEED);
		pathMovement.setLoop(true);
		this.getPhysics().addControlMechanism(pathMovement);
	}


}
