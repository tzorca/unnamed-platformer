package unnamed_platformer.game.other;

import java.util.ArrayList;
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
import unnamed_platformer.game.other.PhysicsInstance;


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

	public static void applyGravity(final ActiveEntity actor,
			final float multiplier) {
		if (!actor.isFlagSet(Flag.OBEYS_GRAVITY)) {
			return;
		}

		actor.getPhysics().addForce(
				new Vector2f(GRAVITY.x * multiplier, GRAVITY.y * multiplier));
	}

	public static void checkForInteractionsWithRegisteredEntities() {
		final List<Entity> possibleInteractors = new ArrayList<Entity>();
		for (final ActiveEntity registeredEntity : registeredEntities) {
			// only check entities in nearby regions
			World.populateFromQuadTree(possibleInteractors,
					registeredEntity.getCollisionRect());

			processInteractions(registeredEntity, possibleInteractors);
			possibleInteractors.clear();
		}
		registeredEntities.clear();

	}

	private static Shape buildCheckShape(Entity entity, Vector2f delta,
			Axis axis) {
		final Shape checkShape = Main.deepClone(entity.getCollisionShape());

		// setup checking rectangle to include either x or y velocity,
		// depending on axis
		if (axis == Axis.HORIZONTAL) {
			checkShape.setX(checkShape.getX() + delta.x);
		} else if (axis == Axis.VERTICAL) {
			checkShape.setY(checkShape.getY() + delta.y);
		}

		return checkShape;
	}

	private static boolean wouldEntityIntersect(
			final ActiveEntity sourceEntity, final Axis direction,
			final Vector2f velocity, final List<Entity> entitiesToCheck) {

		final Shape checkShape = buildCheckShape(sourceEntity, velocity,
				direction);

		for (final Entity otherEntity : entitiesToCheck) {
			if (sourceEntity.equals(otherEntity)) {
				continue;
			}

			if (checkShape.intersects(otherEntity.getCollisionShape())) {
				return true;
			}
		}
		return false;
	}

	private static void performInteractions(final ActiveEntity sourceEntity,
			final Axis direction, final Vector2f velocity,
			final List<Entity> entitiesToCheck) {

		final Shape checkShape = buildCheckShape(sourceEntity, velocity,
				direction);

		PhysicsInstance srcEntityPhysics = sourceEntity.getPhysics();

		for (final Entity otherEntity : entitiesToCheck) {
			if (sourceEntity.equals(otherEntity)) {
				continue;
			}

			if (!checkShape.intersects(otherEntity.getCollisionShape())) {
				continue;
			}

			// the ordering of otherEntity and sourceEntity is important
			if (otherEntity.isActive()) {
				final ActiveEntity activeOtherEntity = (ActiveEntity) otherEntity;
				for (final Interaction interaction : activeOtherEntity.interactions) {
					interaction.interactWith(otherEntity, sourceEntity);
				}
			}
			if (sourceEntity.isActive()) {
				for (final Interaction interaction : sourceEntity.interactions) {
					interaction.interactWith(sourceEntity, otherEntity);
				}
			}

			// don't process collisions if the source entity's physics are
			// currently disabled
			if (srcEntityPhysics.isZero()) {
				continue;
			}
		}
	}

	private static void processInteractions(final ActiveEntity actor,
			final List<Entity> entitiesToCheck) {
		final PhysicsInstance actorPhysics = actor.getPhysics();

		final Vector2f originalPos = actor.getPos();
		final Vector2f velocity = actorPhysics.getVelocity();
		final Vector2f originalVelocity = new Vector2f(velocity);

		applyGlobalSpeedLimit(velocity);

		actorPhysics.lastMoveResult = new MoveResult(originalVelocity.x,
				originalVelocity.y);

		if (actor.isFlagSet(Flag.TANGIBLE)) {
			// if the actor is tangible, we need to check axes separately to
			// determine a safe position before doing interactions.

			final Axis[] axisCheckingOrder;

			if (velocity.x > velocity.y) {
				axisCheckingOrder = new Axis[] {
						Axis.HORIZONTAL, Axis.VERTICAL
				};
			} else {
				axisCheckingOrder = new Axis[] {
						Axis.VERTICAL, Axis.HORIZONTAL
				};
			}

			List<Entity> solidEntities = EntityRef.selectEntitiesWithFlag(
					entitiesToCheck, Flag.SOLID);

			List<Entity> alwaysInteractEntities = EntityRef
					.selectEntitiesWithFlag(entitiesToCheck,
							Flag.ALWAYS_INTERACT);

			for (final Axis axis : axisCheckingOrder) {
				boolean intersectsSolid = wouldEntityIntersect(actor, axis,
						velocity, solidEntities);

				performInteractions(actor, axis, velocity,
						alwaysInteractEntities);
				
				if (actorPhysics.isZero()) {
					break;
				}

				switch (axis) {
				case HORIZONTAL:
					if (intersectsSolid) {
						actorPhysics.setXVelocity(0);
						actorPhysics.lastMoveResult.setXCollision(true);
					} else {
						actor.setX(originalPos.x + velocity.x);
					}
					break;
				case VERTICAL:
					if (intersectsSolid) {
						actorPhysics.setInAir(false);
						actorPhysics.setYVelocity(0);
						actorPhysics.lastMoveResult.setYCollision(true);
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
		} else {
			// Otherwise, we can just perform interactions from the exact
			// velocity that is desired
			actor.setX(originalPos.x + velocity.x);
			actor.setY(originalPos.y + velocity.y);
			if (Math.abs(velocity.y) > MIN_AIR_VELOCITY) {
				actorPhysics.setInAir(true);
			}
		}

		performInteractions(actor, Axis.NONE, new Vector2f(), entitiesToCheck);

		actorPhysics.recalculateDirection(originalPos);
	}

	public static void registerEntityForInteractionChecking(
			final ActiveEntity actor) {
		registeredEntities.add(actor);
	}
}