package game.logic;

import game.PhysicsInstance;
import game.dynamics.interactions.Interaction;
import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.PhysicsRef;
import game.parameters.PhysicsRef.Axis;
import game.parameters.Ref.Flag;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import app.LevelManager;

public class PhysicsProcessor {

	public static Vector2f calculateGravity() {
		return PhysicsRef.gravity;
	}

	public static void applyGravity(ActiveEntity actor, float multiplier) {
		if (!actor.isFlagSet(Flag.obeysGravity)) {
			return;
		}

		actor.getPhysics().addForce(
				new Vector2f(PhysicsRef.gravity.x * multiplier,
						PhysicsRef.gravity.y * multiplier));
	}

	public static void processMoves() {
		for (ActiveEntity a : movedEntities) {

			// only check entities in nearby regions
			List<Entity> entitiesToCheck = new ArrayList<Entity>();
			LevelManager.retrieveFromQuadTree(entitiesToCheck, a.getBox());
			processMove(a, entitiesToCheck);
			entitiesToCheck.clear();
		}
		movedEntities.clear();

	}

	private static boolean findAndProcessInteractions(ActiveEntity a,
			Axis direction, float originalPos, List<Entity> entitiesToCheck) {
		boolean collided = false;
		for (Entity b : entitiesToCheck) {
			if (a != b && a.collidesWith(b)) {
				if (processInteraction(a, b, direction, originalPos)) {
					collided = true;
				}

			}
		}
		return collided;
	}

	private static boolean processInteraction(ActiveEntity a, Entity b,
			Axis direction, float originalPos) {
		if (a.isFlagSet(Flag.hurtsOthers) && b.isFlagSet(Flag.breakableBlock)) {

			b.setFlag(Flag.outOfPlay, true);
			a.setFlag(Flag.outOfPlay, true);
			return false;
		}

		if (a.isFlagSet(Flag.dissolvesOnContact) && b.isFlagSet(Flag.solid)
				&& !b.isFlagSet(Flag.breakableBlock)) {
			a.setFlag(Flag.outOfPlay, true);
			return false;
		}

		// this ordering of b and a is important
		if (b instanceof ActiveEntity) {
			for (Interaction i : ((ActiveEntity) b).interactions) {
				i.interactWith(a);
			}
		}

		if (a.isFlagSet(Flag.tangible) && b.isFlagSet(Flag.solid)
				&& a.hasPhysics()) {
			if (a.getPhysics().isZero()) {
				return false;
			}

			if (direction == Axis.HORIZONTAL) {
				a.setX(originalPos);

				a.getPhysics().setXVelocity(0);
				return true;

			} else {
				PhysicsInstance physics = a.getPhysics();
				physics.inAir = false;
				if (a.getY() < originalPos) {
					physics.upCancel = true;
				}
				a.setY(originalPos);

				a.getPhysics().setYVelocity(0);
				return true;
			}
		}

		if (direction == Axis.VERTICAL && Math.abs(a.getY() - originalPos) > 2) {
			a.getPhysics().inAir = true;
		}

		return false;

	}

	private static void processMove(ActiveEntity actor,
			List<Entity> entitiesToCheck) {

		Vector2f original = actor.getPos();
		Vector2f velocity = actor.getPhysics().getVelocity();
		Vector2f originalVelocity = new Vector2f(velocity);

		applyGlobalSpeedLimit(velocity);

		actor.setX(original.x + velocity.getX());
		boolean xCollision = findAndProcessInteractions(actor, Axis.HORIZONTAL,
				original.x, entitiesToCheck);
		actor.setY(original.y + velocity.getY());
		boolean yCollision = findAndProcessInteractions(actor, Axis.VERTICAL,
				original.y, entitiesToCheck);

		actor.getPhysics().lastMoveResult = new MoveResult(xCollision,
				yCollision, originalVelocity.x, originalVelocity.y);
		actor.getPhysics().recalculateDirection(original);
	}

	private static void applyGlobalSpeedLimit(Vector2f velocity) {
		velocity.x = velocity.x > PhysicsRef.GLOBAL_SPEED_LIMIT ? PhysicsRef.GLOBAL_SPEED_LIMIT
				: velocity.x;
		velocity.y = velocity.y > PhysicsRef.GLOBAL_SPEED_LIMIT ? PhysicsRef.GLOBAL_SPEED_LIMIT
				: velocity.y;

	}

	private static List<ActiveEntity> movedEntities = new ArrayList<ActiveEntity>();

	public static void queueMove(ActiveEntity actor) {
		movedEntities.add(actor);
	}
}
