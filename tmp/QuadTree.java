package structures;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;

import model.entities.Entity;

public class QuadTree {
	Rectangle r;
	QuadTree i, ii, iii, iv;
	@SuppressWarnings("unused")
	private ArrayList<Entity> entities = new LinkedList<Entity>();

	public HashSet<Entity> query(Rectangle window) {
		return null;
	}
}
