package unnamed_platformer.game.dynamics.interactions;

import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.parameters.Ref.Flag;


public class SpringInteraction extends Interaction implements Serializable {
	private static final long serialVersionUID = -990698732640331516L;
	Vector2f v;

	public SpringInteraction(Entity source, Vector2f vector2f) {
		super(source);
		this.v = vector2f;
	}

	@Override
	public void duringInteraction(Entity target) {
		ActiveEntity plr = (ActiveEntity) target;

		if (!plr.hasPhysics()) {
			return;
		}
		plr.getPhysics().upCancel = true;
		plr.getPhysics().addForce(v);
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

}
