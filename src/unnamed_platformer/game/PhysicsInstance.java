package unnamed_platformer.game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.ctrl_methods.ControlMechanism;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.PhysicsRef;
import unnamed_platformer.structures.MoveResult;

public class PhysicsInstance implements Serializable {
	private static final long serialVersionUID = 7637926114689220693L;

	private List<ControlMechanism> mechanisms = new LinkedList<ControlMechanism>();

	private boolean inAir = true;
	private Vector2f lastMove = new Vector2f(0, 0),
			lastHorizontalDirection = new Vector2f(0, 0);
	public boolean upCancel = false;

	Vector2f velocity = new Vector2f(0, 0);
	Vector2f currentForces = new Vector2f();

	private float forceMultiplier = PhysicsRef.DEFAULT_FORCE_MULTIPLIER;

	public MoveResult lastMoveResult = new MoveResult(false, false, 0, 0);

	private ActiveEntity actor;

	public void addForce(org.newdawn.slick.geom.Vector2f v) {
		currentForces = currentForces.add(v);
	}

	public Vector2f getCurrentForce() {
		return currentForces;
	}

	public PhysicsInstance(ActiveEntity actor) {
		this.actor = actor;
	}

	public void addControlMechanism(ControlMechanism mechanism) {
		mechanisms.add(mechanism);
	}

	public void removeControlMechanism(ControlMechanism mechanism) {
		mechanisms.remove(mechanism);
	}

	public void clearControlMechanisms() {
		mechanisms.clear();
	}

	public void update() {
		isZero = false;

		runControlMechanisms();

		if (actor.isFlagSet(Flag.obeysGravity)) {
			PhysicsProcessor.applyGravity(actor, forceMultiplier);
		}

		if (currentForces.equals(PhysicsRef.EMPTY_VECTOR)
				|| currentForces.length() == 0) {
			// do NOT add to collision processing queue (or do anything else)
			return;
		}

		velocity = velocity.add(currentForces);

		// clear out current force (no longer current next tic)
		currentForces = new Vector2f(0, 0);

		inAir = false;
		PhysicsProcessor.queueMove(actor);
		forceMultiplier = PhysicsRef.DEFAULT_FORCE_MULTIPLIER;
	}

	public void recalculateDirection(Vector2f oldPos) {

		Vector2f newPos = actor.getPos();

		if (newPos.x - oldPos.x != 0 || newPos.y - oldPos.y != 0) {
			lastMove = new Vector2f(newPos.x - oldPos.x, newPos.y - oldPos.y);
			if (newPos.x - oldPos.x != 0) {
				lastHorizontalDirection = new Vector2f(lastMove.x, 0);
			}
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

	public Vector2f getLastMove() {
		return lastMove;
	}

	public Vector2f getDirection() {
		return lastHorizontalDirection;
	}

	public void zero() {
		currentForces.x = 0;
		currentForces.y = 0;
		velocity.x = 0;
		velocity.y = 0;

		resetControlMechanisms();
		isZero = true;
	}

	private void resetControlMechanisms() {
		for (ControlMechanism mechanism : mechanisms) {
			mechanism.reset();
		}
	}

	private boolean isZero = false;

	public boolean isZero() {
		return isZero;
	}

	public Vector2f getVelocity() {
		return this.velocity;
	}

	public void setYVelocity(float y) {
		velocity.y = y;
	}

	public void setXVelocity(float x) {
		velocity.x = x;

	}

	public void setForceMultiplier(float factor) {
		forceMultiplier = factor;

	}

	public boolean isOnGround() {
		return !inAir;
	}

	public void setInAir(boolean value) {
		inAir = value;
	}

}
