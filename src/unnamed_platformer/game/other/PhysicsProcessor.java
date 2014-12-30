package unnamed_platformer.game.other;

import java.util.ArrayList;
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
		for (final ActiveEntity actor : registeredEntities) {
			// Collect the nearby entities
			Collection<Entity> nearbyEntities = SpatialHash
					.getNearbyEntities(actor);

			// Move as far as possible while avoiding solid entities in path.
			// TODO: This would involve moving up if a suitable slope exists.
			// Also, save collided entities since they may have interactions.
			List<Entity> collidedEntities = collisionResponse(actor,
					EntityRef.select(nearbyEntities, Flag.SOLID));

			// Perform all interactions occurring the new position.
			doInteractions(actor, nearbyEntities);

			// Do interactions on the saved collided entities.
			for (Entity collidedEntity : collidedEntities) {
				if (collidedEntity.isActive()) {
					ActiveEntity collidedActiveEntity = (ActiveEntity) collidedEntity;
					for (final Interaction interaction : collidedActiveEntity.interactions) {
						interaction.interactWith(collidedActiveEntity, actor);
					}
				}
			}
		}
		registeredEntities.clear();
	}

	private static List<Entity> collisionResponse(ActiveEntity actor,
			List<Entity> solidEntities) {
		List<Entity> colliders = new ArrayList<Entity>();

		final PhysicsInstance actorPhysics = actor.getPhysics();

		final Vector2f originalPos = actor.getPos();
		final Vector2f velocity = actorPhysics.getVelocity();

		if (actor.isFlagSet(Flag.TANGIBLE)) {

			// Axes must be checked separately to determine the best safe
			// position
			final Axis[] orderedAxes;
			if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
				orderedAxes = new Axis[] { Axis.HORIZONTAL, Axis.VERTICAL };
			} else {
				orderedAxes = new Axis[] { Axis.VERTICAL, Axis.HORIZONTAL };
			}

			for (final Axis axis : orderedAxes) {

				if (actorPhysics.isZero()) {
					break;
				}

				Entity intersector = getIntersector(actor, solidEntities, axis,
						velocity);

				if (intersector == null) {
					if (axis == Axis.HORIZONTAL) {
						actor.setX(originalPos.x + velocity.x);
					} else if (axis == Axis.VERTICAL) {
						actor.setY(originalPos.y + velocity.y);
						if (Math.abs(velocity.y) > MIN_AIR_VELOCITY) {
							actorPhysics.setInAir(true);
						}
					}
				} else {
					colliders.add(intersector);
					actorPhysics.handleCollision(axis);
				}
			}
		} else {
			// Don't bother checking solids if entity is intangible
			actor.setX(originalPos.x + velocity.x);
			actor.setY(originalPos.y + velocity.y);
			actorPhysics.setInAir(Math.abs(velocity.y) > MIN_AIR_VELOCITY);
		}

		actorPhysics.recalculateDirection(originalPos);

		return colliders;
	}

	private static void doInteractions(final ActiveEntity actor,
			final Collection<Entity> entitiesToCheck) {

		final Shape checkShape = actor.getCollisionShape();

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

	private static Entity getIntersector(final ActiveEntity actor,
			final List<Entity> otherEntities, final Axis axis,
			final Vector2f velocity) {

		final Shape checkShape = generateCheckShape(actor, velocity, axis);

		for (final Entity otherEntity : otherEntities) {
			if (actor.equals(otherEntity)) {
				continue;
			}

			if (checkShape.intersects(otherEntity.getCollisionShape())) {
				return otherEntity;
			}
		}
		return null;
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

	public static void applyGlobalSpeedLimit(final Vector2f velocity) {
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