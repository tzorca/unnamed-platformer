package game.logic;

import game.dynamics.interactions.Interaction;
import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.PhysicsRef.Axis;
import game.parameters.Ref.Flag;
import game.structures.MoveAttempt;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

public class CollisionProcessor {

	static boolean collisionOccurred = false;

	private static void findAndProcessCollisions(ActiveEntity a,
			Axis direction, int originalPos, List<Entity> entitiesToCheck) {
		for (Entity b : entitiesToCheck) {
			if (a != b && a.collidesWith(b)) {
				process(a, b, direction, originalPos);
			}
		}
	}

	private static void process(ActiveEntity a, Entity b, Axis direction,
			int originalPos) {
		if (a.checkFlag(Flag.hurtsOthers) && b.checkFlag(Flag.breakableBlock)) {

			b.setFlag(Flag.outOfPlay, true);
			a.setFlag(Flag.outOfPlay, true);
			return;
		}

		if (a.checkFlag(Flag.dissolvesOnContact) && b.checkFlag(Flag.solid)
				&& !b.checkFlag(Flag.breakableBlock)) {
			a.setFlag(Flag.outOfPlay, true);
			return;
		}

		// this ordering of b and a is important
		if (b instanceof ActiveEntity) {
			for (Interaction i : ((ActiveEntity) b).interactions) {
				i.interactWith(a);
			}
		}

		if (a.checkFlag(Flag.tangible) && b.checkFlag(Flag.solid)
				&& a.hasPhysics()) {
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

	public static void processMove(Entity a, List<Entity> entitiesToCheck) {
		if (!moveAttempts.containsKey(a)) {
			return;
		}

		MoveAttempt m = moveAttempts.get(a);

		Point original = a.getPos();

		m.actor.setX((int) (original.x + m.velocity.getX()));
		findAndProcessCollisions(m.actor, Axis.HORIZONTAL, original.x,
				entitiesToCheck);
		m.actor.setY((int) (original.y + m.velocity.getY()));
		findAndProcessCollisions(m.actor, Axis.VERTICAL, original.y,
				entitiesToCheck);

		m.actor.physics.recalculateDirection(original);

		moveAttempts.remove(a);
	}

	private static Map<ActiveEntity, MoveAttempt> moveAttempts = new HashMap<ActiveEntity, MoveAttempt>();

	public static void queueMoveAttempt(ActiveEntity actor, Vector2f velocity) {
		moveAttempts.put(actor, new MoveAttempt(actor, velocity));
	}

}
