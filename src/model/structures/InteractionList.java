package model.structures;

import java.io.Serializable;
import java.util.LinkedList;

import model.interactions.Interaction;

public class InteractionList extends LinkedList<Interaction> implements
		Serializable {
	private static final long serialVersionUID = 920197896792364906L;

	public InteractionList() {
		super();
	}

	public InteractionList(Interaction[] interactions) {
		super();
		for (Interaction i : interactions) {
			this.add(i);
		}
	}

	public InteractionList clone() {
		InteractionList il = new InteractionList();

		il.addAll(this);
		return il;
	}
}
