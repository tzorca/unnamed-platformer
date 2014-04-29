package model.interactions;

import java.io.Serializable;

import model.Ref.Flag;
import model.behaviours.PersistentVectorMovement;
import model.entities.ActiveEntity;
import model.entities.Entity;

import org.lwjgl.util.vector.Vector2f;

public class SpringInteraction extends Interaction implements Serializable {
	private static final long serialVersionUID = -990698732640331516L;
	Vector2f v;

	public SpringInteraction(Entity source, Vector2f v) {
		super(source);
		this.v = v;
	}

	@Override
	public void interactWith(Entity target) {
		if (target.checkFlag(Flag.player)) {
			((ActiveEntity) target).addBehaviour(new PersistentVectorMovement(
					v, 500));
		}
	}

}
