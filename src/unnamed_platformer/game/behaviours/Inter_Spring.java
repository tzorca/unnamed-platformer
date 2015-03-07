package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.AudioManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameGlobals.Flag;

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

		AudioManager.playSample("spring");

		if (v.x != 0) {
			plr.getPhysics().setXForce(v.x);
		}
		if (v.y != 0) {
			plr.getPhysics().setYForce(v.y);
		}

		plr.getPhysics().setInAir(true);

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
