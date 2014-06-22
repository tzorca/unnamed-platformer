package unnamed_platformer.game.entities;

import java.util.LinkedList;
import java.util.List;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.PhysicsInstance;
import unnamed_platformer.game.interactions.Interaction;

public abstract class ActiveEntity extends Entity {
	private static final long serialVersionUID = 7803333719264801403L;

	private PhysicsInstance physics;
	public List<Interaction> interactions = new LinkedList<Interaction>();

	public ActiveEntity(EntitySetup entitySetup) {
		super(entitySetup);
		interactions = new LinkedList<Interaction>();
		zIndex = 1;
	}

	public void returnToStart() {
		setPos(this.startPos); // return to starting position

		if (physics != null) {
			this.getPhysics().zero();
		}
	}

	public void update() {
		super.update();
		if (physics != null) {
			getPhysics().update();
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
