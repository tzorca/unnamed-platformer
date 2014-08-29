package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.ActiveEntity;

public abstract class ControlMechanism {

	public boolean toRemove = false;
	public ActiveEntity actor;
	boolean enabled = true;

	public ControlMechanism(ActiveEntity actor) {
		this.actor = actor;
	}

	public final void update(float multiplier) {
		if (isEnabled()) {
			doUpdate(multiplier);
		}
	}

	private boolean isEnabled() {
		return enabled;
	}

	protected abstract void doUpdate(float multiplier);

	public abstract void reset();

	public void disable() {
		this.enabled = false;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() {
		this.enabled = true;
	}
}
