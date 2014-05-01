package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import model.dynamics.control_mechanisms.ControlMechanism;
import model.entities.ActiveEntity;
import model.logic.CollisionProcessor;
import model.logic.PhysicsProcessor;
import model.structures.ControlMechanismList;

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
	Vector2f currentForce = new Vector2f();
	public boolean solidCollisionOccurred = false;

	private ActiveEntity actor;

	public void addForce(Vector2f force) {
		Vector2f.add(currentForce, force, currentForce);
	}

	public Vector2f getCurrentForce() {
		return currentForce;
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

	public void update(float delta) {

		solidCollisionOccurred = false;
		// PhysicsProcessor.doSomethingNew(actor, delta);

		Point original = actor.getPos();

		runControlMechanisms(delta);

		PhysicsProcessor.applyGravity(actor, delta);

		// v = v + a*t
		currentForce.scale(delta);
		currentForce.scale(PhysicsProcessor.FORCE_SCALE);

		velocity = currentForce;

		// clear out current force (no longer current next tic)
		currentForce = new Vector2f(0, 0);

		CollisionProcessor.processMove(actor, velocity);

		Rectangle box = actor.getBox();

		if (box.x - original.x != 0 || box.y - original.y != 0) {
			lastMove = new Vector2f(box.x - original.x, box.y - original.y);
			if (box.x - original.x != 0) {
				lastDirection = new Vector2f(box.x - original.x, 0);
			}
		}
	}

	private void runControlMechanisms(float delta) {
		ControlMechanismList toRemoveList = new ControlMechanismList();
		for (ControlMechanism b : mechanisms) {
			if (b.toRemove) {
				toRemoveList.add(b);
				continue;
			}
			b.update(delta);
		}
		mechanisms.removeAll(toRemoveList);
	}

	public Vector2f getLastMove() {
		return lastMove;
	}

	public Vector2f getDirection() {
		return lastDirection;
	}

}
