// The code for this Quadtree implementation was taken from
// http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
// and was written by Steven Lambert

package unnamed_platformer.structures;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.entities.Entity;

public class QuadTree {

	private int MAX_OBJECTS = 5;
	private int MAX_LEVELS = 40;

	private int level;
	private List<Entity> entities;
	private Rectangle bounds;
	private QuadTree[] nodes;
 
	public QuadTree(int pLevel, Rectangle rect) {
		level = pLevel;
		entities = new ArrayList<Entity>();
		bounds = rect;
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
	public static Rectangle increaseRect(Rectangle pRect) {
		return new Rectangle(pRect.getX() - pRect.getWidth()*2, pRect.getY()-pRect.getHeight()*2, pRect.getWidth()*4, pRect.getHeight()*4);
	}

	/*
	 * Insert the object into the quadtree. If the node exceeds the capacity, it
	 * will split and add all objects to their corresponding nodes.
	 */
	public void insert(Entity e, Rectangle box) {
		if (nodes[0] != null) {
			int index = getIndex(box);

			if (index != -1) {
				nodes[index].insert(e, box);
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
				int index = getIndex(increaseRect(entities.get(i).getCollisionRect()));
				if (index != -1) {
					nodes[index].insert(entities.remove(i), box);
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
