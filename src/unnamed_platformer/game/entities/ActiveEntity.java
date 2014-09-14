package unnamed_platformer.game.entities;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.behaviours.Interaction;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.game.other.PhysicsInstance;
import unnamed_platformer.globals.EntityRef.EntityParam;

public abstract class ActiveEntity extends Entity
{
	protected PhysicsInstance physics;
	public Set<Interaction> interactions = new HashSet<Interaction>();

	public ActiveEntity(EntitySetup entitySetup) {
		super(entitySetup);
		interactions = new HashSet<Interaction>();
		zIndex = 1;
	}

	public void returnToStart() {
		// return to starting position
		setPos((Vector2f) originalSetup.get(EntityParam.LOCATION));

		if (physics != null) {
			getPhysics().zero();
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

	@Override
	public boolean isActive() {
		return true;
	}

}
