package model.structures;

import java.io.Serializable;
import java.util.LinkedList;

import model.behaviours.Behaviour;

public class BehaviourList extends LinkedList<Behaviour> implements
		Serializable {
	private static final long serialVersionUID = 8169294232668455369L;

	public BehaviourList() {
		super();
	}

	public BehaviourList(Behaviour[] behaviours) {
		super();
		for (Behaviour b : behaviours) {
			this.add(b);
		}
	}
}
