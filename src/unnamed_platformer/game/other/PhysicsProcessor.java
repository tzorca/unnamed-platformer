package unnamed_platformer.game.other;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.Main;
import unnamed_platformer.app.MathHelper.Axis;
import unnamed_platformer.game.behaviours.Interaction;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.GameRef.InteractionResult;
import unnamed_platformer.structures.MoveResult;
// TODO: Implement slope physics (see below)
// Implement image mask -> polygon
// 1 - create polygon from coordinates of edge pixels
// 2 - remove points from polygon and see how much area is reduced
// 3 - if area is reduced < x%, allow point removal

// TODO: Fix (separate x/y checking -related) pixel collision bug

public final class PhysicsProcessor
{
	public static final Vector2f GRAVITY = new Vector2f(0, 0.2f);
	public static final float SPEED_LIMIT = 32;
	public static final float FORCE_MULTIPLIER = 0.9f;
	public static final float MIN_AIR_VELOCITY = 2;

	private static Set<ActiveEntity> registeredEntities = new HashSet<ActiveEntity>();

	private static void applyGlobalSpeedLimit(final Vector2f velocity) {
		if (velocity.x > SPEED_LIMIT) {
			velocity.x = SPEED_LIMIT;
		} else if (velocity.x < -SPEED_LIMIT) {
			velocity.x = -SPEED_LIMIT;
		}

		if (velocity.y > SPEED_LIMIT) {
			velocity.y = SPEED_LIMIT;
		} else if (velocity.y < -SPEED_LIMIT) {
			velocity.y = -SPEED_LIMIT;
		}
	}

	public static void applyGravity(final ActiveEntity actor, final float multiplier) {
		if (!actor.isFlagSet(Flag.OBEYS_GRAVITY)) {
			return;
		}

		actor.getPhysics().addForce(new Vector2f(GRAVITY.x * multiplier, GRAVITY.y * multiplier));
	}

	public static void checkForInteractionsWithRegisteredEntities() {
		final List<Entity> possibleInteractors = new ArrayList<Entity>();
		for (final ActiveEntity registeredEntity : registeredEntities) {
			// only check entities in nearby regions
			World.populateFromQuadTree(possibleInteractors, registeredEntity.getCollisionRect());

			processInteractions(registeredEntity, possibleInteractors);
			possibleInteractors.clear();
		}
		registeredEntities.clear();

	}

	private static Set<InteractionResult> collectInteractions(final ActiveEntity sourceEntity, final Axis direction,
			final Vector2f velocity, final List<Entity> entitiesToCheck) {
		final Set<InteractionResult> interactionResults = EnumSet.noneOf(InteractionResult.class);
		final Shape checkShape = Main.deepClone(sourceEntity.getCollisionShape());

		// setup checking rectangle to include either x or y velocity,
		// depending on axis
		if (direction == Axis.HORIZONTAL) {
			checkShape.setX(checkShape.getX() + velocity.x);
		} else if (direction == Axis.VERTICAL) {
			checkShape.setY(checkShape.getY() + velocity.y);
		} else {
			return interactionResults;
		}
		
		final PhysicsInstance srcEntityPhysics = sourceEntity.getPhysics();

		for (final Entity otherEntity : entitiesToCheck) {
			// don't process interactions if the entities are the same
			// or they don't intersect
			if (sourceEntity.equals(otherEntity) || !checkShape.intersects(otherEntity.getCollisionShape())) {
				continue;
			}

			// the ordering of otherEntity and sourceEntity is important
			if (otherEntity.isActive()) {
				final ActiveEntity activeOtherEntity = (ActiveEntity) otherEntity;
				for (final Interaction interaction : activeOtherEntity.interactions) {
					interactionResults.add(interaction.interactWith(sourceEntity));
				}
			}

			// don't process collisions if the source entity's physics are
			// currently disabled
			if (srcEntityPhysics.isZero()) {
				continue;
			}

			// if conditions are right for a solid collision
			if (sourceEntity.isFlagSet(Flag.TANGIBLE) && otherEntity.isFlagSet(Flag.SOLID)) {
				interactionResults.add(InteractionResult.COLLISION);
			}

		}

		return interactionResults;
	}

	private static void processInteractions(final ActiveEntity actor, final List<Entity> entitiesToCheck) {
		final PhysicsInstance actorPhysics = actor.getPhysics();

		final Vector2f originalPos = actor.getPos();
		final Vector2f velocity = actorPhysics.getVelocity();
		final Vector2f originalVelocity = new Vector2f(velocity);

		applyGlobalSpeedLimit(velocity);

		boolean xCollision = false;
		boolean yCollision = false;

		final Axis[] axisCheckingOrder = velocity.x > velocity.y ? new Axis[] {
				Axis.HORIZONTAL, Axis.VERTICAL
		} : new Axis[] {
				Axis.VERTICAL, Axis.HORIZONTAL
		};

		for (final Axis axis : axisCheckingOrder) {
			Set<InteractionResult> interactionResults = collectInteractions(actor, axis, velocity, entitiesToCheck);

			if (interactionResults.contains(InteractionResult.SKIP_PHYSICS)) {
				break;
			}

			switch (axis) {
			case HORIZONTAL:
				if (interactionResults.contains(InteractionResult.COLLISION)) {
					actorPhysics.setXVelocity(0);
					xCollision = true;
				} else {
					actor.setX(originalPos.x + velocity.x);
				}
				break;
			case VERTICAL:
				if (interactionResults.contains(InteractionResult.COLLISION)) {
					actorPhysics.setInAir(false);
					actorPhysics.setYVelocity(0);

					yCollision = true;
				} else {

					actor.setY(originalPos.y + velocity.y);

					if (Math.abs(velocity.y) > MIN_AIR_VELOCITY) {
						actorPhysics.setInAir(true);
					}
				}
				break;
			default:
				break;
			}

		}

		actorPhysics.lastMoveResult = new MoveResult(xCollision, yCollision, originalVelocity.x, originalVelocity.y);

		actorPhysics.recalculateDirection(originalPos);
	}

	public static void registerEntityForInteractionChecking(final ActiveEntity actor) {
		registeredEntities.add(actor);
	}
}