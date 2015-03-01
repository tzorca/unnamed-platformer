package unnamed_platformer.game.behaviours;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Inter_Bumper extends Interaction
{

	public Inter_Bumper() {
		super();
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		ActiveEntity plr = (ActiveEntity) target;

		if (!plr.hasPhysics()) {
			return false;
		}

		
		plr.getPhysics().setXForce(plr.getPhysics().getLastMove().x * -1.0f);
		plr.getPhysics().setYForce(plr.getPhysics().getLastMove().y * -1.0f);

		plr.getPhysics().setInAir(true);

		plr.getPhysics().resetControlMechanisms();

		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
