package model.entities;

import java.awt.Point;

import model.PhysicsInstance;
import model.structures.FlagMap;
import model.structures.InteractionList;

public class ActiveEntity extends Entity {
	private static final long serialVersionUID = 7803333719264801403L;

	public InteractionList interactions = new InteractionList();
	public PhysicsInstance physics;

	public ActiveEntity(String textureName, Point pos, int width, FlagMap flags) {
		super(textureName, pos, width, flags);
		physics = new PhysicsInstance(this);
	}

	public ActiveEntity(String textureName, Point pos, FlagMap flags) {
		super(textureName, pos, flags);
		physics = new PhysicsInstance(this);
	}

	public void returnToStart() {
		setPos(this.startPos); // return to starting position
	}

	public ActiveEntity() {
		super();
	}

	public ActiveEntity(String texName, Point pos) {
		super(texName, pos, new FlagMap());
		physics = new PhysicsInstance(this);
	}

	public void update(float delta) {
		super.update(delta);
		physics.update(delta);
	}

}
