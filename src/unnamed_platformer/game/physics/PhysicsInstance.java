package unnamed_platformer.game.physics;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.behaviours.ControlMechanism;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.physics.DirectionalEnums.Axis;
import unnamed_platformer.globals.GameGlobals.Flag;

public class PhysicsInstance
{

	private final List<ControlMechanism> mechanisms = new LinkedList<ControlMechanism>();

	private boolean inAir = true;
	private Vector2f lastMove = new Vector2f(0, 0),
			lastHorizontalDirection = new Vector2f(1, 0);

	Vector2f velocity = new Vector2f(0, 0);
	Vector2f currentForces = new Vector2f();

	private float forceMultiplier = PhysicsProcessor.FORCE_MULTIPLIER;

	public MoveResult lastMoveResult = new MoveResult(0, 0);

	private ActiveEntity associatedActor;

	private boolean isZero;

	public PhysicsInstance(ActiveEntity actor) {
		this.associatedActor = actor;
	}

	public void addControlMechanism(ControlMechanism mechanism) {
		mechanisms.add(mechanism);
	}

	public void addForce(Vector2f v) {
		currentForces = currentForces.add(v);
	}

	public void clearControlMechanisms() {
		mechanisms.clear();
	}

	public Vector2f getCurrentForce() {
		return currentForces;
	}

	public Vector2f getDirection() {
		return lastHorizontalDirection;
	}

	public boolean movingLeft() {
		return lastHorizontalDirection.x < 0;
	}

	public Vector2f getLastMove() {
		return lastMove;
	}

	public Vector2f getVelocity() {
		return this.velocity;
	}

	public boolean isOnGround() {
		return !inAir;
	}

	public boolean isZero() {
		return isZero;
	}

	public void recalculateDirection(Vector2f oldPos) {

		Vector2f newPos = associatedActor.getPos();

		if (newPos.x - oldPos.x != 0 || newPos.y - oldPos.y != 0) {
			lastMove = new Vector2f(newPos.x - oldPos.x, newPos.y - oldPos.y);
			if (newPos.x - oldPos.x != 0) {
				lastHorizontalDirection = new Vector2f(lastMove.x, 0);
			}
		}
	}

	public void removeControlMechanism(ControlMechanism mechanism) {
		mechanisms.remove(mechanism);
	}

	public void resetControlMechanisms() {
		for (ControlMechanism mechanism : mechanisms) {
			mechanism.reset();
		}
	}

	private void runControlMechanisms() {
		List<ControlMechanism> toRemoveList = new LinkedList<ControlMechanism>();
		for (ControlMechanism b : mechanisms) {
			if (b.toRemove) {
				toRemoveList.add(b);
				continue;
			}
			b.update(forceMultiplier);
		}
		mechanisms.removeAll(toRemoveList);
	}

	public void setForceMultiplier(float factor) {
		forceMultiplier = factor;

	}

	public void setInAir(boolean value) {
		inAir = value;
	}

	public void setXVelocity(float x) {
		velocity.x = x;

	}

	public void setYVelocity(float y) {
		velocity.y = y;
	}

	public void update() {

		isZero = false;

		if (!associatedActor.isFlagSet(Flag.LOCK_CONTROLS)) {
			runControlMechanisms();
		} 
		
		if (associatedActor.isFlagSet(Flag.OBEYS_GRAVITY)) {
			PhysicsProcessor.applyGravity(associatedActor, forceMultiplier);
		}

		if (currentForces.equals(MathHelper.EMPTY_VECTOR)
				|| currentForces.length() == 0) {
			// do NOT add to collision processing queue (or do anything else)
			return;
		}

		velocity = velocity.add(currentForces);

		// clear out current force (no longer current next tic)
		currentForces = new Vector2f(0, 0);

		PhysicsProcessor.registerForInteractionChecking(associatedActor);
		forceMultiplier = PhysicsProcessor.FORCE_MULTIPLIER;

		// Saves the velocity before the speed limit is applied
		lastMoveResult = new MoveResult(velocity.x, velocity.y);

		PhysicsProcessor.applyGlobalSpeedLimit(velocity);
	}

	public void zero() {
		currentForces.x = 0;
		currentForces.y = 0;
		velocity.x = 0;
		velocity.y = 0;

		resetControlMechanisms();
		isZero = true;
	}

	public void handleCollision(Axis axis) {
		if (axis == Axis.HORIZONTAL) {
			setXVelocity(0);
			lastMoveResult.setXCollision(true);
		} else if (axis == Axis.VERTICAL) {
			setInAir(false);
			setYVelocity(0);
			lastMoveResult.setYCollision(true);
		}
	}

	public void setForce(Vector2f force) {
		velocity.x = 0;
		velocity.y = 0;

		currentForces.x = force.x;
		currentForces.y = force.y;
	}

	public void setXForce(float xForce) {
		velocity.x = 0;
		currentForces.x = xForce;
	}

	public void setYForce(float yForce) {
		velocity.y = 0;
		currentForces.y = yForce;
	}

	/**
	 * Prevent movement controls for a certain amount of seconds
	 * 
	 * @param controlLockTime
	 */
	public void lockFor(final float controlLockTime) {
		if (controlLockTime <= 0) {
			return;
		}
		
		final ActiveEntity actor = PhysicsInstance.this.associatedActor;

		if (!actor.isFlagSet(Flag.LOCK_CONTROLS)) {
			actor.setFlag(Flag.LOCK_CONTROLS, true);

			TimeManager.runAfterWaitSeconds(controlLockTime, new Runnable() {
				@Override
				public void run() {
					actor.setFlag(Flag.LOCK_CONTROLS, false);
				}
			});
		}

	}
}
