package unnamed_platformer.game;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.interactions.Interaction;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;
import unnamed_platformer.globals.PhysicsRef;
import unnamed_platformer.globals.PhysicsRef.Axis;
import unnamed_platformer.res_mgt.CloneManager;
import unnamed_platformer.structures.MoveResult;
// TODO: Implement slope physics (see below)
// Implement image mask -> polygon
// 1 - create polygon from coordinates of edge pixels
// 2 - remove points from polygon and see how much area is reduced
// 3 - if area is reduced < x%, allow point removal

// TODO: Fix (separate x/y checking -related) pixel collision bug

public class PhysicsProcessor {

	private static Set<ActiveEntity> registeredEntities = new HashSet<ActiveEntity>();

	private static void applyGlobalSpeedLimit(Vector2f velocity) {
		velocity.x = velocity.x > PhysicsRef.GLOBAL_SPEED_LIMIT ? PhysicsRef.GLOBAL_SPEED_LIMIT
				: velocity.x;
		velocity.x = velocity.x < -PhysicsRef.GLOBAL_SPEED_LIMIT ? -PhysicsRef.GLOBAL_SPEED_LIMIT
				: velocity.x;
		velocity.y = velocity.y > PhysicsRef.GLOBAL_SPEED_LIMIT ? PhysicsRef.GLOBAL_SPEED_LIMIT
				: velocity.y;
		velocity.y = velocity.y < -PhysicsRef.GLOBAL_SPEED_LIMIT ? -PhysicsRef.GLOBAL_SPEED_LIMIT
				: velocity.y;
	}

	public static void applyGravity(ActiveEntity actor, float multiplier) {
		if (!actor.isFlagSet(Flag.OBEYS_GRAVITY)) {
			return;
		}

		actor.getPhysics().addForce(
				new Vector2f(PhysicsRef.gravity.x * multiplier,
						PhysicsRef.gravity.y * multiplier));
	}

	public static void checkForInteractionsWithRegisteredEntities() {
		for (ActiveEntity registeredEntity : registeredEntities) {

			// only check entities in nearby regions
			List<Entity> possibleInteractors = new ArrayList<Entity>();
			GameManager.populateFromQuadTree(possibleInteractors,
					registeredEntity.getCollisionRect());

			processInteractions(registeredEntity, possibleInteractors);
			possibleInteractors.clear();
		}
		registeredEntities.clear();

	}

	private static Set<InteractionResult> collectInteractions(ActiveEntity sourceEntity,
			Axis direction, Vector2f velocity, List<Entity> entitiesToCheck) {
		Set<InteractionResult> interactionResults = EnumSet
				.noneOf(InteractionResult.class);

		// setup checking rectangle to include either x or y velocity,
		// depending on axis
		Shape checkShape = CloneManager.deepClone(sourceEntity.getCollisionShape());
		switch (direction) {
		case HORIZONTAL:
			checkShape.setX(checkShape.getX() + velocity.x);
			break;
		case VERTICAL:
			checkShape.setY(checkShape.getY() + velocity.y);
			break;
		case NONE:
			return interactionResults;
		}

		PhysicsInstance sourceEntityPhysics = sourceEntity.getPhysics();

		for (Entity b : entitiesToCheck) {
			if (sourceEntity != b && checkShape.intersects(b.getCollisionShape())) {
				// the ordering of b and a is important
				if (b instanceof ActiveEntity) {
					for (Interaction i : ((ActiveEntity) b).interactions) {
						InteractionResult result = i.interactWith(sourceEntity);
						if (result != InteractionResult.NO_RESULT) {
							interactionResults.add(result);
						}
					}
				}

				// solid collisions not useful without physics in entity a
				if (sourceEntityPhysics.isZero()) {
					continue;
				}

				// solid collision occurred
				if (sourceEntity.isFlagSet(Flag.TANGIBLE) && b.isFlagSet(Flag.SOLID)) {
					interactionResults
							.add(direction == Axis.HORIZONTAL ? InteractionResult.X_COLLISION
									: InteractionResult.Y_COLLISION);
				}

			}
		}

		return interactionResults;
	}

	private static void processInteractions(ActiveEntity actor,
			List<Entity> entitiesToCheck) {

		PhysicsInstance actorPhysics = actor.getPhysics();

		Vector2f original = actor.getPos();
		Vector2f velocity = actorPhysics.getVelocity();
		Vector2f originalVelocity = new Vector2f(velocity);

		applyGlobalSpeedLimit(velocity);

		boolean xCollision = false, yCollision = false;

		Axis[] axisCheckingOrder = velocity.x > velocity.y ? new Axis[] {
				Axis.HORIZONTAL, Axis.VERTICAL } : new Axis[] { Axis.VERTICAL,
				Axis.HORIZONTAL };

		Set<InteractionResult> interactionResults = EnumSet
				.noneOf(InteractionResult.class);
		for (Axis axis : axisCheckingOrder) {
			interactionResults.addAll(collectInteractions(actor, axis,
					velocity, entitiesToCheck));

			if (interactionResults.contains(InteractionResult.SKIP_PHYSICS)) {
				continue;
			}
			
			switch (axis) {
			case HORIZONTAL:
				if (interactionResults.contains(InteractionResult.X_COLLISION)) {
					actorPhysics.setXVelocity(0);
					xCollision = true;
				} else {
					actor.setX(original.x + velocity.x);
				}
				break;
			case VERTICAL:
				if (interactionResults.contains(InteractionResult.Y_COLLISION)) {
					actorPhysics.setInAir(false);
					actorPhysics.setYVelocity(0);

					yCollision = true;
				} else {
					actor.setY(original.y + velocity.y);

					if (Math.abs(velocity.y) > 2) {
						actorPhysics.setInAir(true);
					}
				}
				break;
			default:
				break;
			}

		}

		actorPhysics.lastMoveResult = new MoveResult(xCollision, yCollision,
				originalVelocity.x, originalVelocity.y);
		
		actorPhysics.recalculateDirection(original);
	}

	public static void registerEntityForInteractionChecking(ActiveEntity actor) {
		registeredEntities.add(actor);
	}
}