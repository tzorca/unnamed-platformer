package game.logic.levelGenerators;

import game.Level;
import game.entities.Entity;
import game.logic.EntityCreator;
import game.logic.MathHelper;
import game.parameters.Ref;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import app.ViewManager;

public abstract class LevelGenerator {

	protected abstract void internalBuild();

	public Level generate() {
		internalBuild();
		Level lvl = new Level(entities);
		entities = new LinkedList<Entity>();
		return lvl;
	}

	protected double[] rndSizeMuls = { 1, 1, 2, 2, 4 };

	protected int grid = Ref.DEFAULT_LEVEL_GRIDSIZE;

	public int getGrid() {
		return grid;
	}

	public void setGrid(int grid) {
		this.grid = grid;
	}

	public Rectangle getLevelRect() {
		return levelRect;
	}

	public void setLevelRect(Rectangle levelRect) {
		this.levelRect = levelRect;
	}

	protected Rectangle levelRect = new Rectangle(0, 0, MathHelper.randRange(
			grid * 64, grid * 128), MathHelper.randRange(grid * 16, grid * 64));

	private HashMap<String, Entity> distinctEntityMap = new HashMap<String, Entity>();
	private LinkedList<Entity> entities = new LinkedList<Entity>();

	protected Entity add(String texture, int x, int y) {
		return add(texture, x, y, false);
	}

	protected Entity add(Entity newEntity) {
		return add(newEntity, false);
	}

	protected Entity addDistinct(String texture, int x, int y) {
		return add(texture, x, y, true);
	}

	protected Entity add(String texture, int x, int y, int grid) {
		return add(EntityCreator.create(texture, new Point(x, y), grid), false);
	}

	private Entity add(String texture, int x, int y, boolean distinct) {
		return add(EntityCreator.create(texture, new Point(x, y)), distinct);
	}

	private Entity add(Entity newEntity, boolean distinct) {
		String texture = newEntity.getTextureName();
		if (distinct) {
			if (distinctEntityMap.containsKey(texture)) {
				entities.remove(distinctEntityMap.get(texture));
			}

			distinctEntityMap.put(texture, newEntity);
		}

		entities.add(newEntity);
		return newEntity;
	}

	protected boolean entityIntersectsDistinct(Entity entity) {
		for (Entity dEntity : distinctEntityMap.values()) {
			if (dEntity.collidesWith(entity)) {
				return true;
			}
		}
		return false;
	}

	protected List<Entity> addLevelEdges(String borderTexture) {
		List<Entity> newEntities = new ArrayList<Entity>();
		int maxX = levelRect.width;
		int maxY = levelRect.height;
		int borderBlockSize = 128;

		// draw vertical edges
		for (int i = -ViewManager.height; i <= maxY + ViewManager.height; i += borderBlockSize) {
			Point posT = new Point(-borderBlockSize, i);
			Point posB = new Point(maxX, i);
			for (int j = 0; j <= ViewManager.height; j += borderBlockSize) {
				Point posTadj = new Point(posT.x - j, posT.y);
				Point posBadj = new Point(posB.x + j, posB.y);

				newEntities.add(EntityCreator.create(borderTexture, posTadj,
						borderBlockSize, false));
				newEntities.add(EntityCreator.create(borderTexture, posBadj,
						borderBlockSize, false));
			}
		}

		// draw horizontal edges
		for (int i = -ViewManager.width; i <= maxX + ViewManager.width; i += borderBlockSize) {

			Point posL = new Point(i, -borderBlockSize);
			Point posR = new Point(i, maxY);

			for (int j = 0; j <= ViewManager.width; j += borderBlockSize) {
				Point posLadj = new Point(posL.x, posL.y - j);
				Point posRadj = new Point(posR.x, posR.y + j);

				newEntities.add(EntityCreator.create(borderTexture, posLadj,
						borderBlockSize, false));
				newEntities.add(EntityCreator.create(borderTexture, posRadj,
						borderBlockSize, false));
			}
		}

		entities.addAll(newEntities);
		return newEntities;
	}

}
