package unnamed_platformer.game.interactions;

import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;


public class Interaction_Spring extends Interaction implements Serializable {
	private static final long serialVersionUID = -990698732640331516L;
	Vector2f v;

	public Interaction_Spring(Entity source, Vector2f vector2f) {
		super(source);
		this.v = vector2f;
	}

	@Override
	public InteractionResult performInteraction(Entity target) {
		ActiveEntity plr = (ActiveEntity) target;

		if (!plr.hasPhysics()) {
			return InteractionResult.NO_RESULT;
		}
		plr.getPhysics().addForce(v);

		return InteractionResult.NO_RESULT;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
