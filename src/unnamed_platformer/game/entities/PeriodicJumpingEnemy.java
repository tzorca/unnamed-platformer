package unnamed_platformer.game.entities;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.behaviours.Ctrl_PathMovement;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.GameGlobals;

public class PeriodicJumpingEnemy extends Hazard {

	private Ctrl_PathMovement pathMovement;

	public PeriodicJumpingEnemy(EntitySetup entitySetup) {
		super(entitySetup);
		addPathMovement();
	}

	private void addPathMovement() {
		pathMovement = new Ctrl_PathMovement(this, this.startPos,
				MathHelper.createUpDownPath(GameGlobals.FLAME_JUMP_HEIGHT),
				GameGlobals.FLAME_SPEED);
		pathMovement.setLoop(false);
		this.getPhysics().addControlMechanism(pathMovement);
	}

	@Override
	public void update() {
		super.update();

		if (TimeManager.periodElapsed(this, "flameJump",
				GameGlobals.FLAME_JUMP_INTERVAL)) {
			pathMovement.reset();
		}

	}

}
