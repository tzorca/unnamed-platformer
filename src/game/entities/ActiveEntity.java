package game.entities;

import game.PhysicsInstance;
import game.parameters.Ref.Flag;
import game.structures.Graphic;
import game.structures.InteractionList;

import java.awt.Point;
import java.util.EnumSet;

public class ActiveEntity extends Entity {
	private static final long serialVersionUID = 7803333719264801403L;

	public InteractionList interactions = new InteractionList();
	private PhysicsInstance physics;

	public ActiveEntity(Graphic graphic, Point pos, int width,
			EnumSet<Flag> flags) {
		super(graphic, pos, width, flags);
	}

	public ActiveEntity(Graphic graphic, Point pos, EnumSet<Flag> flags) {
		super(graphic, pos, flags);
	}

	public void returnToStart() {
		setPos(this.startPos); // return to starting position

		if (physics != null) {
			this.getPhysics().zero();
		}
	}

	public ActiveEntity() {
		super();
	}

	public ActiveEntity(Graphic graphic, Point pos) {
		super(graphic, pos, EnumSet.noneOf(Flag.class));
	}

	public void update(long millisecDelta) {
		super.update(millisecDelta);
		if (physics != null) {
			getPhysics().update(millisecDelta);
		}
	}

	public PhysicsInstance getPhysics() {
		if (this.physics == null) {
			addPhysics();
		}

		return physics;
	}

	public void addPhysics() {
		this.physics = new PhysicsInstance(this);
	}

	public boolean hasPhysics() {
		return this.physics != null;
	}

}
