package game.structures;

import game.dynamics.control_mechanisms.ControlMechanism;

import java.io.Serializable;
import java.util.LinkedList;

public class ControlMechanismList extends LinkedList<ControlMechanism> implements
		Serializable {
	private static final long serialVersionUID = 8169294232668455369L;

	public ControlMechanismList() {
		super();
	}

	public ControlMechanismList(ControlMechanism[] behaviours) {
		super();
		for (ControlMechanism b : behaviours) {
			this.add(b);
		}
	}
}
