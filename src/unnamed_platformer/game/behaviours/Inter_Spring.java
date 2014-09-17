package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;


public class Inter_Spring extends Interaction {
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
		plr.getPhysics().addForce(v);

		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
