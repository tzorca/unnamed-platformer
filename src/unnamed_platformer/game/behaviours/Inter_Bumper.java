package unnamed_platformer.game.behaviours;

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
		
		Double angle = getIntersectionAngle();
		
		plr.getPhysics().setXForce((float)(7 * Math.cos(angle)));
		plr.getPhysics().setYForce((float)(7 * -Math.sin(angle)));

		plr.getPhysics().setInAir(true);

		plr.getPhysics().resetControlMechanisms();

		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
