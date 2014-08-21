package unnamed_platformer.game;

import java.util.ArrayList;
import java.util.EnumSet;
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

	public static Vector2f calculateGravity() {
		return PhysicsRef.gravity;
	}

	public static void applyGravity(ActiveEntity actor, float multiplier) {
		if (!actor.isFlagSet(Flag.obeysGravity)) {
			return;
		}

		actor.getPhysics().addForce(new Vector2f(PhysicsRef.gravity.x * multiplier, PhysicsRef.gravity.y * multiplier));
	}

	public static void processMoves() {
		for (ActiveEntity a : movedEntities) {

			// only check entities in nearby regions
			List<Entity> entitiesToCheck = new ArrayList<Entity>();
			GameManager.retrieveFromQuadTree(entitiesToCheck, a.getCollisionRect());
			processMove(a, entitiesToCheck);
			entitiesToCheck.clear();
		}
		movedEntities.clear();

	}

	private static Set<InteractionResult> findAndProcessInteractions(ActiveEntity a, Axis direction, Vector2f velocity,
			List<Entity> entitiesToCheck) {
		Set<InteractionResult> interactionResults = EnumSet.noneOf(InteractionResult.class);

		// setup checking rectangle to include either x or y velocity,
		// depending on axis
		Shape checkShape = CloneManager.deepClone(a.getCollisionShape());
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

		for (Entity b : entitiesToCheck) {
			if (a != b && checkShape.intersects(b.getCollisionShape())) {
				interactionResults.addAll(processInteraction(a, b, direction));
			}
		}

		return interactionResults;
	}

	private static Set<InteractionResult> processInteraction(ActiveEntity a, Entity b, Axis direction) {
		EnumSet<InteractionResult> interactionResults = EnumSet.noneOf(InteractionResult.class);

		// the ordering of b and a is important
		if (b instanceof ActiveEntity) {
			for (Interaction i : ((ActiveEntity) b).interactions) {
				InteractionResult result = i.interactWith(a);
				if (result != InteractionResult.NO_RESULT) {
					interactionResults.add(result);
				}
			}
		}

		// physics not applicable in this scenario
		if (!a.hasPhysics() || a.getPhysics().isZero()) {
			return interactionResults;
		}

		// if a solid collision occurred
		if (a.isFlagSet(Flag.tangible) && b.isFlagSet(Flag.solid)) {
			interactionResults.add(direction == Axis.HORIZONTAL ? InteractionResult.X_COLLISION
					: InteractionResult.Y_COLLISION);
		}

		return interactionResults;
	}


	private static void processMove(ActiveEntity actor, List<Entity> entitiesToCheck) {

		Vector2f original = actor.getPos();
		Vector2f velocity = actor.getPhysics().getVelocity();
		Vector2f originalVelocity = new Vector2f(velocity);

		applyGlobalSpeedLimit(velocity);

		boolean xCollision = false, yCollision = false;

		Axis[] axisCheckingOrder = velocity.x > velocity.y ? new Axis[] {
				Axis.HORIZONTAL, Axis.VERTICAL
		} : new Axis[] {
				Axis.VERTICAL, Axis.HORIZONTAL
		};

		Set<InteractionResult> interactionResults = EnumSet.noneOf(InteractionResult.class);
		for (Axis axis : axisCheckingOrder) {
			interactionResults.addAll(findAndProcessInteractions(actor, axis, velocity, entitiesToCheck));

			if (!interactionResults.contains(InteractionResult.SKIP_PHYSICS)) {
				switch (axis) {
				case HORIZONTAL:
					if (interactionResults.contains(InteractionResult.X_COLLISION)) {
						actor.getPhysics().setXVelocity(0);
						xCollision = true;
					} else {
						actor.setX(original.x + velocity.x);
					}
					break;
				case VERTICAL:
					if (interactionResults.contains(InteractionResult.Y_COLLISION)) {
						PhysicsInstance physics = actor.getPhysics();
						physics.inAir = false;
						if (velocity.y < 0) {
							physics.upCancel = true;
						}

						physics.setYVelocity(0);

						yCollision = true;
					} else {
						actor.setY(original.y + velocity.y);

						if (Math.abs(velocity.y) > 2) {
							actor.getPhysics().inAir = true;
						}
					}
					break;
				default:
					break;
				}
			}

		}

		actor.getPhysics().lastMoveResult = new MoveResult(xCollision, yCollision, originalVelocity.x,
				originalVelocity.y);
		actor.getPhysics().recalculateDirection(original);
	}

	private static void applyGlobalSpeedLimit(Vector2f velocity) {
		velocity.x = velocity.x > PhysicsRef.GLOBAL_SPEED_LIMIT ? PhysicsRef.GLOBAL_SPEED_LIMIT : velocity.x;
		velocity.x = velocity.x < -PhysicsRef.GLOBAL_SPEED_LIMIT ? -PhysicsRef.GLOBAL_SPEED_LIMIT : velocity.x;
		velocity.y = velocity.y > PhysicsRef.GLOBAL_SPEED_LIMIT ? PhysicsRef.GLOBAL_SPEED_LIMIT : velocity.y;
		velocity.y = velocity.y < -PhysicsRef.GLOBAL_SPEED_LIMIT ? -PhysicsRef.GLOBAL_SPEED_LIMIT : velocity.y;

	}

	private static List<ActiveEntity> movedEntities = new ArrayList<ActiveEntity>();

	public static void queueMove(ActiveEntity actor) {
		movedEntities.add(actor);
	}
}