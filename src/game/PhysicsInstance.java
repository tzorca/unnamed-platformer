package game;

import game.dynamics.control_mechanisms.ControlMechanism;
import game.entities.ActiveEntity;
import game.logic.CollisionProcessor;
import game.logic.PhysicsProcessor;
import game.parameters.PhysicsRef;
import game.parameters.Ref.Flag;
import game.structures.ControlMechanismList;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class PhysicsInstance implements Serializable {
	private static final long serialVersionUID = 7637926114689220693L;

	private List<ControlMechanism> mechanisms = new LinkedList<ControlMechanism>();

	public boolean inAir = true;
	public double airTime = 0;
	private Vector2f lastMove = new Vector2f(0, 0),
			lastDirection = new Vector2f(0, 0);
	public boolean upCancel = false;

	Vector2f velocity = new Vector2f(0, 0);
	Vector2f currentForceConstruction = new Vector2f();
	public boolean solidCollisionOccurred = false;

	private ActiveEntity actor;

	public void addForce(Vector2f force) {
		Vector2f.add(currentForceConstruction, force, currentForceConstruction);
	}

	public Vector2f getCurrentForce() {
		return currentForceConstruction;
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

	// TODO: Still needs some work...
	public void update(long millisecDelta) {

		solidCollisionOccurred = false;
		isZero = false;

		runControlMechanisms(millisecDelta);

		if (actor.isFlagSet(Flag.obeysGravity)) {
			PhysicsProcessor.applyGravity(actor, millisecDelta);
		}

		if (currentForceConstruction.equals(PhysicsRef.EMPTY_VECTOR)
				|| currentForceConstruction.length() == 0) {
			// do NOT add to collision processing queue (or do anything else)
			return;
		}

		// v = v + a*t
		// TODO: This formula is not even used properly!
		// Lots of restructuring in other places will be needed to fix it...

		velocity = (Vector2f) currentForceConstruction.scale(millisecDelta);

		// clear out current force (no longer current next tic)
		currentForceConstruction = new Vector2f(0, 0);

		CollisionProcessor.queue(actor);
	}

	public void recalculateDirection(Point original) {
		Rectangle box = actor.getBox();

		if (box.x - original.x != 0 || box.y - original.y != 0) {
			lastMove = new Vector2f(box.x - original.x, box.y - original.y);
			if (box.x - original.x != 0) {
				lastDirection = new Vector2f(box.x - original.x, 0);
			}
		}
	}

	private void runControlMechanisms(long millisecDelta) {
		ControlMechanismList toRemoveList = new ControlMechanismList();
		for (ControlMechanism b : mechanisms) {
			if (b.toRemove) {
				toRemoveList.add(b);
				continue;
			}
			b.update(millisecDelta);
		}
		mechanisms.removeAll(toRemoveList);
	}

	public Vector2f getLastMove() {
		return lastMove;
	}

	public Vector2f getDirection() {
		return lastDirection;
	}

	public void zero() {
		currentForceConstruction.x = 0;
		currentForceConstruction.y = 0;
		airTime = 0;
		velocity.x = 0;
		velocity.y = 0;
		solidCollisionOccurred = false;

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

}
