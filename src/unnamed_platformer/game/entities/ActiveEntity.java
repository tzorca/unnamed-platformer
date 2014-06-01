package unnamed_platformer.game.entities;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.PhysicsInstance;
import unnamed_platformer.game.structures.InteractionList;

public abstract class ActiveEntity extends Entity {
	private static final long serialVersionUID = 7803333719264801403L;

	private PhysicsInstance physics;
	public InteractionList interactions = new InteractionList();

	public ActiveEntity(EntitySetup entitySetup) {
		super(entitySetup);
		interactions = new InteractionList();
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
