package unnamed_platformer.game.behaviours;

import java.io.Serializable;
import java.util.EnumMap;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.MathHelper.Side;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.InteractionResult;

public abstract class Interaction implements Serializable {
	private static final long serialVersionUID = -2402822368545376501L;

	public static final double LOOSE_SIDE_MATCH_DISTANCE = Math.PI / 2;
	public static final double STRICT_SIDE_MATCH_DISTANCE = Math.PI / 6;

	private Side[] activeSides;
	private double maxSideMatchDistance = 0;

	Entity source;

	public Interaction(Entity source) {
		this.source = source;
		activeSides = Side.values();
	}

	public Interaction(Entity source, Side[] activeSides,
			boolean strictSideMatching) {
		this.source = source;

		this.activeSides = activeSides;

		if (strictSideMatching) {
			maxSideMatchDistance = STRICT_SIDE_MATCH_DISTANCE;
		} else {
			maxSideMatchDistance = LOOSE_SIDE_MATCH_DISTANCE;
		}

	}

	public final InteractionResult interactWith(Entity target) {
		if (!isValidTarget(target)) {
			return InteractionResult.NO_RESULT;
		}

		if (!onActiveside(target)) {
			return InteractionResult.NO_RESULT;
		}

		return performInteraction(target);
	}

	private boolean onActiveside(Entity target) {
		// All sides are active! Don't do math!
		if (activeSides.length == Side.values().length) {
			return true;
		}

		Double intersectionAngle = MathHelper.getIntersectionAngle(
				source.getOriginalBox(), target.getOriginalBox());

		EnumMap<Side, Double> sideDistances = MathHelper.getSideDistances(
				intersectionAngle, activeSides);

		for (Side side : activeSides) {
			if (sideDistances.get(side) <= maxSideMatchDistance) {

				return true;
			}
		}

		return false;
	}

	protected abstract boolean isValidTarget(Entity target);

	protected abstract InteractionResult performInteraction(Entity target);
}
