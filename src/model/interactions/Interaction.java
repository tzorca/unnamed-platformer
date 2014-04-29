package model.interactions;

import java.io.Serializable;

import model.entities.Entity;

public abstract class Interaction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2402822368545376501L;
	Entity source;
	public Interaction(Entity source) {
		this.source = source;
	}
	
	public abstract void interactWith(Entity target);
}
