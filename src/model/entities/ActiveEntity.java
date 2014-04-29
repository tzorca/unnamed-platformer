// Tim Zorca
// CPSC 3520
package model.entities;

import java.awt.Point;

import model.Ref.Flag;
import model.behaviours.Behaviour;
import model.interactions.Interaction;
import model.logic.CollisionProcessor;
import model.logic.PhysicsProcessor;
import model.structures.BehaviourList;
import model.structures.FlagMap;
import model.structures.InteractionList;

import org.lwjgl.util.vector.Vector2f;

public class ActiveEntity extends Entity {

	private static final long serialVersionUID = 7803333719264801403L;
	InteractionList interactions = new InteractionList();
	BehaviourList behaviours = new BehaviourList();
	public boolean inAir = true;
	public double airTime = 0;
	private Vector2f lastMove = new Vector2f(0, 0),
			lastDirection = new Vector2f(0, 0);
	public boolean upCancel = false;

	public ActiveEntity(String textureName, Point pos, int width,
			FlagMap flags, BehaviourList behaviours) {
		super(textureName, pos, width, flags);
	}

	public ActiveEntity(String textureName, Point pos, FlagMap flags,
			BehaviourList behaviours) {
		super(textureName, pos, flags);
		this.behaviours = behaviours;
	}

	public void returnToStart() {
		this.setPos(this.startPos);
	}

	public ActiveEntity() {
		super();
	}

	public ActiveEntity(String texName, Point pos) {
		super(texName, pos, new FlagMap());
	}

	public ActiveEntity clone() {
		return new ActiveEntity(textureName, getPos(), box.width,
				this.flags.clone(), this.behaviours.clone());
	}

	Vector2f velocity = new Vector2f(0, 0); // pixels/ms
	// Vector2f accel; // pixels/ms^2
	// Vector2f force; // mass*pixels/ms^2
	float mass = 1f;
	Vector2f currentForce = new Vector2f();
	public boolean lastTicSolidCollision = false;

	public void addForce(Vector2f force) {
		// may need to come back and incorporate time later
		Vector2f.add(currentForce, force, currentForce);
	}

	public Vector2f getCurrentForce() {
		return currentForce;
	}

	public void addInteraction(Interaction interaction) {
		interactions.add(interaction);
	}

	public void removeInteraction(Interaction interaction) {
		interactions.remove(interaction);
	}

	public void clearInteraction(Interaction interaction) {
		interactions.clear();
	}

	public void addBehaviour(Behaviour behaviour) {
		behaviours.add(behaviour);
	}

	public void removeBehaviour(Behaviour behaviour) {
		behaviours.remove(behaviour);
	}

	public void clearBehaviours() {
		behaviours.clear();
	}

	public void update(float delta) {
		Point original = getPos();
		box.x += velocity.getX() * delta;
		CollisionProcessor.checkAndFix(this, CollisionProcessor.HORIZONTAL,
				original.x);
		box.y += velocity.getY() * delta;
		CollisionProcessor.checkAndFix(this, CollisionProcessor.VERTICAL,
				original.y);

		runBehaviours(delta);

		applyGravity(delta);

		if (box.x - original.x != 0 || box.y - original.y != 0) {
			lastMove = new Vector2f(box.x - original.x, box.y - original.y);
			if (box.x - original.x != 0) {
				lastDirection = new Vector2f(box.x - original.x, 0);
			}
		}

		// a = F/m
		currentForce.scale(1f / mass);
		// v = v + a*t
		currentForce.scale(delta);
		currentForce.scale(PhysicsProcessor.FORCE_SCALE);
		// velocity = currentForce;
		Vector2f.add(velocity, currentForce, velocity);

		velocity = currentForce;

		// clear out current force (no longer current next tic)
		currentForce = new Vector2f(0, 0);

		super.update(delta);
	}

	private void runBehaviours(float delta) {
		BehaviourList toRemove = new BehaviourList();
		for (Behaviour b : behaviours) {
			if (b.toRemove) {
				toRemove.add(b);
				continue;
			}
			b.run(this, delta);
		}
		behaviours.removeAll(toRemove);
	}

	private void applyGravity(float delta) {
		if (checkFlag(Flag.obeysGravity)) {

			addForce(PhysicsProcessor.calculateGravity(airTime));
			inAir = true;
			airTime += delta / 1000.0;
		}
	}

	public Vector2f getLastMove() {
		return lastMove;
	}

	public Vector2f getDirection() {
		return lastDirection;
	}

	public InteractionList getInteractions() {
		return interactions;
	}

	public boolean isDynamic() {
		return (!this.flags.isEmpty() || !this.interactions.isEmpty());
	}

}
