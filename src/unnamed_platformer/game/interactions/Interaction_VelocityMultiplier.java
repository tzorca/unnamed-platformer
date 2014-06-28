package unnamed_platformer.game.interactions;

import unnamed_platformer.game.PhysicsInstance;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;

public class Interaction_VelocityMultiplier extends Interaction {
	private static final long serialVersionUID = 5830114835277305660L;

	private float factor; 
	
	public Interaction_VelocityMultiplier(Entity source, float factor) {
		super(source);
		this.factor = factor;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

	@Override
	protected InteractionResult performInteraction(Entity target) {

		PhysicsInstance physics = ((ActiveEntity) target).getPhysics();
		physics.setForceMultiplier(factor);
//		
		return InteractionResult.NO_RESULT;
//		Vector2f addVector = new Vector2f();
//		Vector2f currentForce =physics.getCurrentForce();
//		Vector2f.sub(currentForce, (Vector2f) currentForce.scale(factor), addVector);
//		
//		physics.addForce(addVector);
//		
	}

}
