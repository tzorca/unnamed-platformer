package unnamed_platformer.game.behaviours;

import java.util.EnumMap;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.physics.DirectionalEnums.Side;

public abstract class Interaction
{

	public static final double LOOSE_SIDE_MATCH_DISTANCE = Math.PI / 2;
	public static final double STRICT_SIDE_MATCH_DISTANCE = Math.PI / 4;

	private Side[] activeSides;
	private double maxSideMatchDistance = 0;

	public Interaction() {
		activeSides = Side.values();
	}

	public Interaction(Entity source, Side[] activeSides,
			boolean strictSideMatching) {
		this.activeSides = activeSides;

		if (strictSideMatching) {
			maxSideMatchDistance = STRICT_SIDE_MATCH_DISTANCE;
		} else {
			maxSideMatchDistance = LOOSE_SIDE_MATCH_DISTANCE;
		}

	}

	public final boolean interactWith(Entity source, Entity target) {
		if (!isValidTarget(target)) {
			return false;
		}

		if (!onActiveside(source, target)) {
			return false;
		}
		
		return performInteraction(source, target);
	}

	private boolean onActiveside(Entity source, Entity target) {
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

	protected abstract boolean performInteraction(Entity source, Entity target);
}
