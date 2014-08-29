package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.behaviours.Ctrl_PathMovement;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameRef;

public class JumpingHazard extends Hazard {
	private static final int FLAME_PERIOD_ID = 1;

	private Ctrl_PathMovement pathMovement;

	public JumpingHazard(EntitySetup entitySetup) {
		super(entitySetup);
		addPathMovement();
	}

	private void addPathMovement() {
		pathMovement = new Ctrl_PathMovement(this, this.startPos,
				MathHelper.createUpDownPath(GameRef.DEFAULT_FLAME_JUMP_HEIGHT), GameRef.DEFAULT_FLAME_SPEED);
		pathMovement.setLoop(false);
		// pathMovement.disable();
		this.getPhysics().addControlMechanism(pathMovement);
	}

	@Override
	public void update() {
		super.update();

		if (TimeManager.periodElapsed(this.hashCode(), FLAME_PERIOD_ID, GameRef.DEFAULT_FLAME_JUMP_INTERVAL)) {
			// pathMovement.enable();
			pathMovement.reset();
		}

	}

}
