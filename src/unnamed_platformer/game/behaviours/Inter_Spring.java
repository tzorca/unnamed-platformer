package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.res_mgt.SoundManager;

public class Inter_Spring extends Interaction
{
	Vector2f v;

	public Inter_Spring(Vector2f vector2f) {
		super();
		this.v = vector2f;
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		ActiveEntity plr = (ActiveEntity) target;

		if (!plr.hasPhysics()) {
			return false;
		}

		SoundManager.playSample("spring");

		plr.getPhysics().setVerticalForce(v);
		plr.getPhysics().setInAir(true);
		plr.moveAbove(target);

		// TODO: Revisit this and think about how to deal with
		// setting the jumping flag more intelligently.
		plr.getPhysics().resetControlMechanisms();

		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
