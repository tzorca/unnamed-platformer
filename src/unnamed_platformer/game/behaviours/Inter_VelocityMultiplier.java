package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.other.PhysicsInstance;
import unnamed_platformer.globals.GameRef.Flag;

public class Inter_VelocityMultiplier extends Interaction {

	private float factor; 
	
	public Inter_VelocityMultiplier(float factor) {
		super();
		this.factor = factor;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {

		PhysicsInstance physics = ((ActiveEntity) target).getPhysics();
		physics.setForceMultiplier(factor);
//		
		return true;
//		Vector2f addVector = new Vector2f();
//		Vector2f currentForce =physics.getCurrentForce();
//		Vector2f.sub(currentForce, (Vector2f) currentForce.scale(factor), addVector);
//		
//		physics.addForce(addVector);
//		
	}

}
