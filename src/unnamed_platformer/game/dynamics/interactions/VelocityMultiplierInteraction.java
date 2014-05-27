package unnamed_platformer.game.dynamics.interactions;

import unnamed_platformer.game.PhysicsInstance;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.parameters.Ref.Flag;

public class VelocityMultiplierInteraction extends Interaction {
	private static final long serialVersionUID = 5830114835277305660L;

	private float factor; 
	
	public VelocityMultiplierInteraction(Entity source, float factor) {
		super(source);
		this.factor = factor;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.player);
	}

	@Override
	protected void duringInteraction(Entity target) {

		PhysicsInstance physics = ((ActiveEntity) target).getPhysics();
		physics.setForceMultiplier(factor);
//		
//		Vector2f addVector = new Vector2f();
//		Vector2f currentForce =physics.getCurrentForce();
//		Vector2f.sub(currentForce, (Vector2f) currentForce.scale(factor), addVector);
//		
//		physics.addForce(addVector);
//		
	}

}