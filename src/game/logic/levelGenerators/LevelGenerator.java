package game.logic.levelGenerators;

import game.Level;
import game.entities.Entity;
import game.logic.EntityCreator;
import game.parameters.EntityRef.EntityType;
import game.parameters.Ref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import app.ViewManager;

public abstract class LevelGenerator {

	protected abstract void internalBuild();

	public Level generate() {
		internalBuild();
		Level lvl = new Level(entities, levelRect);
		entities = new LinkedList<Entity>();
		return lvl;
	}

	protected double[] rndSizeMuls = { 1, 1, 2, 2, 4 };

	protected int grid = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private Rectangle levelRect = Ref.DEFAULT_LEVEL_RECTANGLE;

	private HashMap<String, Entity> distinctEntityMap = new HashMap<String, Entity>();
	private LinkedList<Entity> entities = new LinkedList<Entity>();

	protected Entity add(String texture, int x, int y) {
		updateLevelRect(x, y);

		return add(texture, x, y, false);
	}

	protected Entity add(Entity newEntity) {
		updateLevelRect(newEntity.getPos());

		return add(newEntity, false);
	}

	protected Entity addDistinct(String textureName, int x, int y) {
		updateLevelRect(x, y);

		return add(textureName, x, y, true);
	}

	private static final int EXPECTED_MAX_ENTITY_SIZE = 256;

	private void updateLevelRect(Vector2f pos) {

		if (levelRect.getWidth() < pos.x + EXPECTED_MAX_ENTITY_SIZE) {
			levelRect.setWidth(pos.x + EXPECTED_MAX_ENTITY_SIZE);
		}

		if (levelRect.getX() > pos.x - EXPECTED_MAX_ENTITY_SIZE) {
			float expansion = Math.abs(levelRect.getX()
					- (pos.x - EXPECTED_MAX_ENTITY_SIZE));
			levelRect.setX(pos.x - EXPECTED_MAX_ENTITY_SIZE);
			levelRect.setWidth(levelRect.getWidth() + expansion);
		}

		if (levelRect.getWidth() < pos.y + EXPECTED_MAX_ENTITY_SIZE) {
			levelRect.setWidth(pos.y + EXPECTED_MAX_ENTITY_SIZE);
		}

		if (levelRect.getY() > pos.y - EXPECTED_MAX_ENTITY_SIZE) {
			float expansion = Math.abs(levelRect.getY()
					- (pos.y - EXPECTED_MAX_ENTITY_SIZE));
			levelRect.setY(pos.y - EXPECTED_MAX_ENTITY_SIZE);
			levelRect.setHeight(levelRect.getHeight() + expansion);
		}
	}

	protected Entity add(EntityType type, float x, float y, int grid) {
		updateLevelRect(x, y);

		return add(EntityCreator.create(type, new Vector2f(x, y), grid, false));
	}

	private void updateLevelRect(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	protected Entity add(String textureName, float x, float y, int grid) {
		updateLevelRect(x, y);

		return add(EntityCreator.create(textureName, new Vector2f(x, y), grid),
				false);
	}

	private Entity add(String textureName, float x, float y, boolean distinct) {
		updateLevelRect(x, y);

		return add(EntityCreator.create(textureName, new Vector2f(x, y)), distinct);
	}

	private Entity add(Entity newEntity, boolean distinct) {
		updateLevelRect(newEntity.getX(), newEntity.getY());

		String texture = newEntity.graphic.getTextureName();
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

	protected void addLevelEdges(String borderTexture) {
		List<Entity> newEntities = new ArrayList<Entity>();
		float maxX = levelRect.getWidth();
		float maxY = levelRect.getHeight();
		int borderBlockSize = 128;

		// draw vertical edges
		for (int i = -ViewManager.height; i <= maxY + ViewManager.height; i += borderBlockSize) {
			Vector2f posT = new Vector2f(-borderBlockSize, i);
			Vector2f posB = new Vector2f(maxX, i);
			for (int j = 0; j <= ViewManager.height; j += borderBlockSize) {
				Vector2f posTadj = new Vector2f(posT.x - j, posT.y);
				Vector2f posBadj = new Vector2f(posB.x + j, posB.y);

				newEntities.add(EntityCreator.create(borderTexture, posTadj,
						borderBlockSize, false));
				newEntities.add(EntityCreator.create(borderTexture, posBadj,
						borderBlockSize, false));
			}
		}

		// draw horizontal edges
		for (int i = -ViewManager.width; i <= maxX + ViewManager.width; i += borderBlockSize) {

			Vector2f posL = new Vector2f(i, -borderBlockSize);
			Vector2f posR = new Vector2f(i, maxY);

			for (int j = 0; j <= ViewManager.width; j += borderBlockSize) {
				Vector2f posLadj = new Vector2f(posL.x, posL.y - j);
				Vector2f posRadj = new Vector2f(posR.x, posR.y + j);

				newEntities.add(EntityCreator.create(borderTexture, posLadj,
						borderBlockSize, false));
				newEntities.add(EntityCreator.create(borderTexture, posRadj,
						borderBlockSize, false));
			}
		}

		for (Entity newEntity : newEntities) {
			add(newEntity);
		}

	}

}
