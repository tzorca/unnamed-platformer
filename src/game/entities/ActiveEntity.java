package game.entities;

import game.PhysicsInstance;
import game.structures.FlagMap;
import game.structures.Graphic;
import game.structures.InteractionList;

import java.awt.Point;

public class ActiveEntity extends Entity {
	private static final long serialVersionUID = 7803333719264801403L;

	public InteractionList interactions = new InteractionList();
	public PhysicsInstance physics;

	public ActiveEntity(Graphic graphic, Point pos, int width, FlagMap flags) {
		super(graphic, pos, width, flags);
		physics = new PhysicsInstance(this);
	}

	public ActiveEntity(Graphic graphic, Point pos, FlagMap flags) {
		super(graphic, pos, flags);
		physics = new PhysicsInstance(this);
	}

	public void returnToStart() {
		setPos(this.startPos); // return to starting position
	}

	public ActiveEntity() {
		super();
	}

	public ActiveEntity(Graphic graphic, Point pos) {
		super(graphic, pos, new FlagMap());
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
