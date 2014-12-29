package unnamed_platformer.game.other;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.Main;
import unnamed_platformer.game.behaviours.Interaction;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.other.DirectionalEnums.Axis;
import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.GameRef.Flag;

public final class PhysicsProcessor
{
	public static final Vector2f GRAVITY = new Vector2f(0, 0.2f);
	public static final float SPEED_LIMIT = 32;
	public static final float FORCE_MULTIPLIER = 0.9f;
	public static final float MIN_AIR_VELOCITY = 2;

	private static Set<ActiveEntity> registeredEntities = new HashSet<ActiveEntity>();

	public static void registerForInteractionChecking(final ActiveEntity actor) {
		registeredEntities.add(actor);
	}

	public static void processInteractionsNearRegisteredEntities() {
		for (final ActiveEntity registeredEntity : registeredEntities) {
			processInteractions(registeredEntity,
					SpatialHash.getNearbyEntities(registeredEntity));
		}
		registeredEntities.clear();
	}

	private static void processInteractions(final ActiveEntity actor,
			final Collection<Entity> entities) {
		final PhysicsInstance actorPhysics = actor.getPhysics();

		final Vector2f originalPos = actor.getPos();
		final Vector2f velocity = actorPhysics.getVelocity();

		// Saves the velocity before the speed limit is applied
		actorPhysics.lastMoveResult = new MoveResult(velocity.x, velocity.y);

		applyGlobalSpeedLimit(velocity);

		if (actor.isFlagSet(Flag.TANGIBLE)) {
			// if the actor is tangible, axes must be checked separately to
			// determine a safe position before performing interactions

			final Axis[] orderedAxes;
			if (velocity.x > velocity.y) {
				orderedAxes = new Axis[] { Axis.HORIZONTAL, Axis.VERTICAL };
			} else {
				orderedAxes = new Axis[] { Axis.VERTICAL, Axis.HORIZONTAL };
			}

			List<Entity> solidEntities = EntityRef.select(entities, Flag.SOLID);

			List<Entity> alwaysInteractEntities = EntityRef.select(entities,
					Flag.ALWAYS_INTERACT);

			for (final Axis axis : orderedAxes) {
				boolean intersectsSolid = wouldIntersect(actor, solidEntities,
						axis, velocity);

				doInteractions(actor, axis, velocity, alwaysInteractEntities);

				if (actorPhysics.isZero()) {
					break;
				}

				if (intersectsSolid) {
					actorPhysics.handleCollision(axis);
				} else {
					if (axis == Axis.HORIZONTAL) {
						actor.setX(originalPos.x + velocity.x);
					} else if (axis == Axis.VERTICAL) {
						actor.setY(originalPos.y + velocity.y);
						if (Math.abs(velocity.y) > MIN_AIR_VELOCITY) {
							actorPhysics.setInAir(true);
						}
					}
				}
			}
		} else {
			// If the actor is not tangible, interactions can use exact velocity
			actor.setX(originalPos.x + velocity.x);
			actor.setY(originalPos.y + velocity.y);
			actorPhysics.setInAir(Math.abs(velocity.y) > MIN_AIR_VELOCITY);
		}

		doInteractions(actor, Axis.NONE, new Vector2f(), entities);

		actorPhysics.recalculateDirection(originalPos);
	}

	private static void doInteractions(final ActiveEntity actor,
			final Axis axis, final Vector2f velocity,
			final Collection<Entity> entitiesToCheck) {

		final Shape checkShape = generateCheckShape(actor, velocity, axis);

		for (final Entity otherEntity : entitiesToCheck) {
			if (actor.equals(otherEntity)) {
				continue;
			}

			if (!checkShape.intersects(otherEntity.getCollisionShape())) {
				continue;
			}

			// the ordering of otherEntity and actor is important

			if (otherEntity.isActive()) {
				final ActiveEntity activeOtherEntity = (ActiveEntity) otherEntity;
				for (final Interaction interaction : activeOtherEntity.interactions) {
					interaction.interactWith(otherEntity, actor);
				}
			}

			for (final Interaction interaction : actor.interactions) {
				interaction.interactWith(actor, otherEntity);
			}
		}
	}

	private static Shape generateCheckShape(Entity entity, Vector2f velocity,
			Axis axis) {
		final Shape checkShape = Main.deepClone(entity.getCollisionShape());

		if (axis == Axis.HORIZONTAL) {
			checkShape.setX(checkShape.getX() + velocity.x);
		} else if (axis == Axis.VERTICAL) {
			checkShape.setY(checkShape.getY() + velocity.y);
		}

		return checkShape;
	}

	private static boolean wouldIntersect(final ActiveEntity actor,
			final List<Entity> otherEntities, final Axis axis,
			final Vector2f velocity) {

		final Shape checkShape = generateCheckShape(actor, velocity, axis);

		for (final Entity otherEntity : otherEntities) {
			if (actor.equals(otherEntity)) {
				continue;
			}

			if (checkShape.intersects(otherEntity.getCollisionShape())) {
				return true;
			}
		}
		return false;
	}

	private static void applyGlobalSpeedLimit(final Vector2f velocity) {
		velocity.x = Math.min(velocity.x, SPEED_LIMIT);
		velocity.x = Math.max(velocity.x, -SPEED_LIMIT);
		velocity.y = Math.min(velocity.y, SPEED_LIMIT);
		velocity.y = Math.max(velocity.y, -SPEED_LIMIT);
	}

	public static void applyGravity(final ActiveEntity actor,
			final float multiplier) {
		if (!actor.isFlagSet(Flag.OBEYS_GRAVITY)) {
			return;
		}

		actor.getPhysics().addForce(
				new Vector2f(GRAVITY.x * multiplier, GRAVITY.y * multiplier));
	}

}