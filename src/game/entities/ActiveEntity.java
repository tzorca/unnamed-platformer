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
	public PhysicsInstance physics;

	public ActiveEntity(Graphic graphic, Point pos, int width,
			EnumSet<Flag> flags) {
		super(graphic, pos, width, flags);
		physics = new PhysicsInstance(this);
	}

	public ActiveEntity(Graphic graphic, Point pos, EnumSet<Flag> flags) {
		super(graphic, pos, flags);
		physics = new PhysicsInstance(this);
	}

	public void returnToStart() {
		setPos(this.startPos); // return to starting position

		if (hasPhysics()) {
			this.physics.zero();
		}
	}

	public ActiveEntity() {
		super();
	}

	public ActiveEntity(Graphic graphic, Point pos) {
		super(graphic, pos, EnumSet.noneOf(Flag.class));
		physics = new PhysicsInstance(this);
	}

	public void update(long millisecDelta) {
		super.update(millisecDelta);
		if (this.hasPhysics()) {
			physics.update(millisecDelta);
		}
	}

	public boolean hasPhysics() {
		return physics != null;
	}

}
