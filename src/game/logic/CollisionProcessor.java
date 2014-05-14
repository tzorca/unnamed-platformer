package game.logic;

import game.dynamics.interactions.Interaction;
import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.PhysicsRef.Axis;
import game.parameters.Ref.Flag;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import app.LevelManager;

public class CollisionProcessor {

	static boolean collisionOccurred = false;

	public static void processMoves() {
		for (ActiveEntity a : movedEntities) {

			// only check entities in nearby regions
			List<Entity> entitiesToCheck = new ArrayList<Entity>();
			LevelManager.retrieveFromQuadTree(entitiesToCheck, a.getBox());
			CollisionProcessor.processMove(a, entitiesToCheck);
			entitiesToCheck.clear();
		}
		movedEntities.clear();

	}

	private static void findAndProcessCollisions(ActiveEntity a,
			Axis direction, int originalPos, List<Entity> entitiesToCheck) {
		for (Entity b : entitiesToCheck) {
			if (a != b && a.collidesWith(b)) {
				processCollision(a, b, direction, originalPos);
			}
		}
	}

	private static void processCollision(ActiveEntity a, Entity b,
			Axis direction, int originalPos) {
		if (a.isFlagSet(Flag.hurtsOthers) && b.isFlagSet(Flag.breakableBlock)) {

			b.setFlag(Flag.outOfPlay, true);
			a.setFlag(Flag.outOfPlay, true);
			return;
		}

		if (a.isFlagSet(Flag.dissolvesOnContact) && b.isFlagSet(Flag.solid)
				&& !b.isFlagSet(Flag.breakableBlock)) {
			a.setFlag(Flag.outOfPlay, true);
			return;
		}

		// this ordering of b and a is important
		if (b instanceof ActiveEntity) {
			for (Interaction i : ((ActiveEntity) b).interactions) {
				i.interactWith(a);
			}
		}

		if (a.isFlagSet(Flag.tangible) && b.isFlagSet(Flag.solid)
				&& a.hasPhysics()) {
			if (a.physics.isZero()) {
				return;
			}

			if (direction != Axis.HORIZONTAL) {
				a.physics.inAir = false;
				a.physics.airTime = 0;
				if (a.getY() < originalPos) {
					a.physics.upCancel = true;
				}

				a.setY(originalPos);
				a.physics.solidCollisionOccurred = true;

			} else {
				a.setX(originalPos);
				a.physics.solidCollisionOccurred = true;
			}
		}

	}

	private static void processMove(ActiveEntity actor,
			List<Entity> entitiesToCheck) {

		Point original = actor.getPos();
		Vector2f velocity = actor.physics.getVelocity();

		actor.setX((int) (original.x + velocity.getX()));
		findAndProcessCollisions(actor, Axis.HORIZONTAL, original.x,
				entitiesToCheck);
		actor.setY((int) (original.y + velocity.getY()));
		findAndProcessCollisions(actor, Axis.VERTICAL, original.y,
				entitiesToCheck);

		actor.physics.recalculateDirection(original);
	}

	private static List<ActiveEntity> movedEntities = new ArrayList<ActiveEntity>();

	public static void queue(ActiveEntity actor) {
		movedEntities.add(actor);
	}

}
