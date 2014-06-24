package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.ctrl_methods.Control_PathMovement;
import unnamed_platformer.globals.PhysicsRef;

public class JumpingFlame extends Hazard {
	private static final long serialVersionUID = -4355060532556741847L;
	private static final int FLAME_PERIOD_ID = 1;

	private Control_PathMovement pathMovement;

	public JumpingFlame(EntitySetup entitySetup) {
		super(entitySetup);
		addPathMovement();
	}

	private void addPathMovement() {
		pathMovement = new Control_PathMovement(this, this.startPos,
				MathHelper.createUpDownPath(PhysicsRef.DEFAULT_FLAME_JUMP_HEIGHT), PhysicsRef.DEFAULT_FLAME_SPEED);
		pathMovement.setLoop(false);
		// pathMovement.disable();
		this.getPhysics().addControlMechanism(pathMovement);
	}

	@Override
	public void update() {
		super.update();

		if (TimeManager.periodElapsed(this.hashCode(), FLAME_PERIOD_ID, PhysicsRef.DEFAULT_FLAME_JUMP_INTERVAL)) {
			// pathMovement.enable();
			pathMovement.reset();
		}

	}

}
