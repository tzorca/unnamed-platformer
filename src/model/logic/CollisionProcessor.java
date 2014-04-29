package model.logic;

import model.Ref.Flag;
import model.entities.ActiveEntity;
import model.entities.Entity;
import model.interactions.Interaction;
import app.App;
import app.LevelManager;

public class CollisionProcessor {
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	public static boolean springchecking = false;

	public static void checkAndFix(ActiveEntity a, int direction,
			int originalPos) {
		springchecking = false;
		for (Entity other : LevelManager.getCurrentEntities()) {
			if (other == a || !a.collidesWith(other)) {
				continue;
			}

			if (!other.isDynamic()) {
				continue;
			}

			process(a, other, direction, originalPos);
		}
	}

	private static void process(ActiveEntity a, Entity b, int direction,
			int originalPos) {

		a.lastTicSolidCollision = false;
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
				a.inAir = false;
				a.airTime = 0;
				if (a.getY() < originalPos) {
					a.upCancel = true;
				}

				a.setY(originalPos);
				a.lastTicSolidCollision = true;

			} else {
				a.setX(originalPos);
				a.lastTicSolidCollision = true;
			}
		}

		if (a instanceof ActiveEntity) {
			for (Interaction i : ((ActiveEntity) a).getInteractions()) {
				i.interactWith(b);
			}
		}

	}

}
