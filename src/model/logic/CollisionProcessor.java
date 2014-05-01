package model.logic;

import java.awt.Point;

import org.lwjgl.util.vector.Vector2f;

import model.Ref.Flag;
import model.dynamics.interactions.Interaction;
import model.entities.ActiveEntity;
import model.entities.Entity;
import app.App;
import app.LevelManager;

public class CollisionProcessor {
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	public static boolean springchecking = false;

	private static void checkAndFix(ActiveEntity a, int direction,
			int originalPos) {
		springchecking = false;
		for (Entity other : LevelManager.getCurrentEntities()) {
			if (other == a || !a.collidesWith(other)) {
				continue;
			}

			process(a, other, direction, originalPos);
		}
	}

	private static void process(ActiveEntity a, Entity b, int direction,
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
			if (direction != HORIZONTAL) {
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
		CollisionProcessor.checkAndFix(actor, CollisionProcessor.HORIZONTAL,
				original.x);
		actor.setY((int) (original.y + velocity.getY()));
		CollisionProcessor.checkAndFix(actor, CollisionProcessor.VERTICAL,
				original.y);
	}

}
