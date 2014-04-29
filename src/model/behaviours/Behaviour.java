// Tim Zorca
// CPSC 3520
package model.behaviours;

import java.io.Serializable;

import model.entities.ActiveEntity;

public abstract class Behaviour implements Serializable {
	private static final long serialVersionUID = -7400186683325692152L;
	
	public abstract void run(ActiveEntity target, float delta);
	public boolean toRemove = false;

}
