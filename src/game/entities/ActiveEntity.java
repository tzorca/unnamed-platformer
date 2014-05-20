package game.entities;

import game.PhysicsInstance;
import game.structures.Graphic;
import game.structures.InteractionList;

import org.newdawn.slick.geom.Vector2f;

public abstract class ActiveEntity extends Entity {
	private static final long serialVersionUID = 7803333719264801403L;

	private PhysicsInstance physics;
	public InteractionList interactions = new InteractionList();

	public ActiveEntity(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
		interactions = new InteractionList();
		zIndex = 1;
		defaultActiveSetup();
	}

	public ActiveEntity(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
		interactions = new InteractionList();
		zIndex = 1;
		defaultActiveSetup();
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

	protected abstract void defaultActiveSetup();

	@Override
	protected void defaultSetup() {

	}
}
