package unnamed_platformer.game.dynamics.control_mechanisms;

import java.io.Serializable;

import unnamed_platformer.game.entities.ActiveEntity;

public abstract class ControlMechanism implements Serializable {
	private static final long serialVersionUID = -7400186683325692152L;

	public boolean toRemove = false;
	public ActiveEntity actor;

	public ControlMechanism(ActiveEntity actor) {
		this.actor = actor;
	}

	public abstract void update(float multiplier);

	public abstract void reset();
}
