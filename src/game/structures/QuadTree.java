// The code for this Quadtree implementation was taken from
// http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
// and was written by Steven Lambert

package game.structures;

import game.entities.Entity;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class QuadTree {

	private int MAX_OBJECTS = 20;
	private int MAX_LEVELS = 10;

	private int level;
	private List<Entity> entities;
	private Rectangle bounds;
	private QuadTree[] nodes;

	public QuadTree(int pLevel, Rectangle pBounds) {
		level = pLevel;
		entities = new ArrayList<Entity>();
		bounds = pBounds;
		nodes = new QuadTree[4];
	}

	public void clear() {
		entities.clear();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	// Splits the node into 4 subnodes
	private void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();

		nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y,
				subWidth, subHeight));
		nodes[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth,
				subHeight));
		nodes[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight,
				subWidth, subHeight));
		nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y
				+ subHeight, subWidth, subHeight));
	}

	/*
	 * Determine which node the object belongs to. -1 means object cannot
	 * completely fit within a child node and is part of the parent node
	 */
	private int getIndex(Rectangle pRect) {
		int index = -1;

		pRect = increaseRect(pRect);
		

		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect
				.getY() + pRect.getHeight() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

		// Object can completely fit within the left quadrants
		if (pRect.getX() < verticalMidpoint
				&& pRect.getX() + pRect.getWidth() < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (pRect.getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	// prevent stupid "just barely out of range so fall through ground" bugs
	private Rectangle increaseRect(Rectangle pRect) {
		return new Rectangle(pRect.x-32, pRect.y-32, pRect.width+64, pRect.height+64);
	}

	/*
	 * Insert the object into the quadtree. If the node exceeds the capacity, it
	 * will split and add all objects to their corresponding nodes.
	 */
	public void insert(Entity e) {
		if (nodes[0] != null) {
			int index = getIndex(increaseRect(e.getBox()));

			if (index != -1) {
				nodes[index].insert(e);
				return;
			}
		}

		entities.add(e);

		if (entities.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < entities.size()) {
				int index = getIndex(increaseRect(entities.get(i).getBox()));
				if (index != -1) {
					nodes[index].insert(entities.remove(i));
				} else {
					i++;
				}
			}
		}
	}

	// Return all objects that could collide with the given object
	public List<Entity> retrieve(List<Entity> returnObjects, Rectangle pRect) {
		int index = getIndex(pRect);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, pRect);
		}

		returnObjects.addAll(entities);

		return returnObjects;
	}
}
