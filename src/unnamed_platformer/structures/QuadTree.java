// The code for this Quadtree implementation was taken from
// http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
// and was written by Steven Lambert

package unnamed_platformer.structures;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.entities.Entity;

public class QuadTree
{

	private final static int MAX_OBJECTS = 5;
	private final static int MAX_LEVELS = 40;

	private final int level;
	private final List<Entity> entities;
	private final Rectangle bounds;
	private QuadTree[] nodes;

	public QuadTree(final int pLevel, final Rectangle rect) {
		level = pLevel;
		entities = new ArrayList<Entity>();
		bounds = rect;
		nodes = new QuadTree[4];
	}

	public void clear() {
		entities.clear();

		for (final QuadTree node : nodes) {
			if (node == null) {
				continue;
			}

			if (node.entities != null) {
				node.entities.clear();
			}
			node.clear();
		}
	}

	// Splits the node into 4 subnodes
	private void split() {
		final int subWidth = (int) (bounds.getWidth() / 2);
		final int subHeight = (int) (bounds.getHeight() / 2);
		final int xPos = (int) bounds.getX();
		final int yPos = (int) bounds.getY();

		nodes[0] = new QuadTree(level + 1, new Rectangle(xPos + subWidth, yPos, subWidth, subHeight));
		nodes[1] = new QuadTree(level + 1, new Rectangle(xPos, yPos, subWidth, subHeight));
		nodes[2] = new QuadTree(level + 1, new Rectangle(xPos, yPos + subHeight, subWidth, subHeight));
		nodes[3] = new QuadTree(level + 1, new Rectangle(xPos + subWidth, yPos + subHeight, subWidth, subHeight));
	}

	/*
	 * Determine which node the object belongs to. -1 means object cannot
	 * completely fit within a child node and is part of the parent node
	 */
	private int getIndex(final Rectangle pRect) {
		int index = -1;

		final double vertMidpoint = bounds.getX() + bounds.getWidth() / 2;
		final double horzMidpoint = bounds.getY() + bounds.getHeight() / 2;

		// Object can completely fit within the top quadrants
		final boolean topQuadrant = pRect.getY() < horzMidpoint && pRect.getY() + pRect.getHeight() < horzMidpoint;

		// Object can completely fit within the bottom quadrants
		final boolean bottomQuadrant = pRect.getY() > horzMidpoint;

		// Object can completely fit within the left quadrants
		if (pRect.getX() < vertMidpoint && pRect.getX() + pRect.getWidth() < vertMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (pRect.getX() > vertMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	// prevent stupid bugs
	public static Rectangle increaseRect(final Rectangle pRect) {
		return new Rectangle(pRect.getX() - pRect.getWidth(), pRect.getY() - pRect.getHeight(), pRect.getWidth() * 2,
				pRect.getHeight() * 2);
	}

	/*
	 * Insert the object into the quadtree. If the node exceeds the capacity, it
	 * will split and add all objects to their corresponding nodes.
	 */
	public void insert(final Entity entity, final Rectangle box) {
		if (nodes[0] != null) {
			final int index = getIndex(box);

			if (index != -1) {
				nodes[index].insert(entity, box);
				return;
			}
		}

		entities.add(entity);

		if (entities.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < entities.size()) {
				final int index = getIndex(increaseRect(entities.get(i).getCollisionRect()));
				if (index != -1) {
					nodes[index].insert(entities.remove(i), box);
				} else {
					i++;
				}
			}
		}
	}

	// Return all objects that could collide with the given object
	public List<Entity> retrieve(final List<Entity> returnObjects, final Rectangle pRect) {
		final int index = getIndex(pRect);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, pRect);
		}

		returnObjects.addAll(entities);

		return returnObjects;
	}
}
