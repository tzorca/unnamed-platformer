package game.logic;

import game.dynamics.interactions.Interaction;
import game.entities.ActiveEntity;
import game.entities.Entity;
import game.parameters.PhysicsRef.Axis;
import game.parameters.Ref.Flag;

import java.awt.Point;

import org.lwjgl.util.vector.Vector2f;

import app.App;
import app.LevelManager;

public class CollisionProcessor {

	private static void checkAndFix(ActiveEntity a, Axis direction,
			int originalPos) {
		for (Entity other : LevelManager.getCurrentEntities()) {
			if (other == a || !a.collidesWith(other)) {
				continue;
			}

			process(a, other, direction, originalPos);
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

		if (a.checkFlag(Flag.player) && b.checkFlag(Flag.levelGoal)) {
			App.requestRestart();
			return;
		}

		if (a.checkFlag(Flag.tangible) && b.checkFlag(Flag.solid)) {
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

		if (a instanceof ActiveEntity) {
			for (Interaction i : ((ActiveEntity) a).interactions) {
				i.interactWith(b);
			}
		}

	}

	public static void processMove(ActiveEntity actor, Vector2f velocity) {
		Point original = actor.getPos();

		actor.setX((int) (original.x + velocity.getX()));
		checkAndFix(actor, Axis.HORIZONTAL, original.x);
		actor.setY((int) (original.y + velocity.getY()));
		checkAndFix(actor, Axis.VERTICAL, original.y);
	}

}
