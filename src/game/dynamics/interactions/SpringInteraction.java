package game.dynamics.interactions;

import game.dynamics.control_mechanisms.Control_PersistentVectorMovement;
import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.Ref.Flag;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

public class SpringInteraction extends Interaction implements Serializable {
	private static final long serialVersionUID = -990698732640331516L;
	Vector2f v;

	public SpringInteraction(Entity source, Vector2f v) {
		super(source);
		this.v = v;
	}

	@Override
	public void performInteractionAction(Entity target) {
		ActiveEntity plr = (ActiveEntity) target;

		if (!plr.hasPhysics()) {
			return;
		}
		plr.getPhysics().airTime = 0;
		plr.getPhysics().upCancel = true;
		plr.getPhysics().addForce(v);
		plr.getPhysics().addControlMechanism(new Control_PersistentVectorMovement(
				plr, v, 500));
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

}
