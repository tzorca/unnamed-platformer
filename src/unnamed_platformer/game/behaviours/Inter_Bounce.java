package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.physics.PhysicsInstance;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Inter_Bounce extends Interaction
{
	private static final float MINIMUM_SPEED = 2;

	private static final float LOCK_SPEED_TIME_SCALE = 0.025f;

	private float bounceStrength;
	private float controlLockTime = 0;
	private boolean dynamic;

	/**
	 * Creates a dynamic Inter_Bounce
	 */
	public Inter_Bounce() {
		super();
		this.dynamic = true;
	}

	/**
	 * Creates a static Inter_Bounce
	 * 
	 * @param bounceStrength
	 * @param controlLockTime
	 */
	public Inter_Bounce(float bounceStrength, float controlLockTime) {
		super();
		this.dynamic = false;
		this.bounceStrength = bounceStrength;
		this.controlLockTime = controlLockTime;
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {

		// Get reaction vector
		Vector2f reactionVector = calculateReactionVector(source, target);

		// Apply reaction vector
		PhysicsInstance playerPhysics = ((ActiveEntity) target).getPhysics();
		playerPhysics.setXForce(reactionVector.x);
		playerPhysics.setYForce(reactionVector.y);
		playerPhysics.setInAir(true);
		playerPhysics.resetControlMechanisms();

		// Lock movement for certain amount of time
		if (dynamic) {
			float speed = MathHelper.getSpeed(reactionVector);
			playerPhysics.lockFor(speed * LOCK_SPEED_TIME_SCALE);
		} else {
			playerPhysics.lockFor(controlLockTime);
		}

		// Play sound effect
		int springSampleNo = MathHelper.randRange(0, 9);
		AudioManager.playSample("spring" + springSampleNo);

		return true;
	}

	private Vector2f calculateReactionVector(Entity source, Entity target) {
		Shape sourceShape = source.getCollisionShape();
		
		// If possible, use collision location as the target.
		Shape targetShape;
//		Shape[] union = sourceShape.union(target.getCollisionShape());
//		if (union.length == 1) {
//			targetShape = union[0];
//		} else {
			targetShape = target.getCollisionShape();
//		}
		
		Vector2f sourceToTarget = MathHelper.centerToCenter(
				sourceShape, targetShape);
		Vector2f reactionVector = sourceToTarget.getNormal().negate();

		if (dynamic) {
			float speed = MathHelper.getSpeed(((ActiveEntity) target)
					.getPhysics().getLastMove());
			speed = Math.max(speed, MINIMUM_SPEED);
			reactionVector = reactionVector.scale(speed * 1.01f);
		} else {
			reactionVector = reactionVector.scale(bounceStrength);
		}
		return reactionVector;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER)
				&& ((ActiveEntity) target).hasPhysics();
	}

}
