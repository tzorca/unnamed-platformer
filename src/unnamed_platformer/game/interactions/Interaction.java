package unnamed_platformer.game.interactions;

import java.io.Serializable;
import java.util.EnumMap;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.PhysicsRef;
import unnamed_platformer.globals.PhysicsRef.Side;

public abstract class Interaction implements Serializable {
	private static final long serialVersionUID = -2402822368545376501L;

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
			maxSideMatchDistance = PhysicsRef.STRICT_SIDE_MATCH_DISTANCE;
		} else {
			maxSideMatchDistance = PhysicsRef.LOOSE_SIDE_MATCH_DISTANCE;
		}

	}

	public final void interactWith(Entity target) {
		if (!isValidTarget(target)) {
			return;
		}

		if (!onActiveside(target)) {
			return;
		}

		duringInteraction(target);
	}

	private boolean onActiveside(Entity target) {
		// All sides are active! Don't do math!
		if (activeSides.length == Side.values().length) {
			return true;
		}

		Double intersectionAngle = MathHelper.getIntersectionAngle(
				source.getBox(), target.getBox());

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

	protected abstract void duringInteraction(Entity target);
//	
//	protected abstract void beforeInteraction(Entity target);
//	
//	protected abstract void afterInteraction(Entity target);

}
