package unnamed_platformer.game.lvl_gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.EntityCreator;
import unnamed_platformer.game.Level;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.Ref;

public abstract class BaseLevelGenerator {

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
		updateLevelRect(newEntity.getPos().x, newEntity.getPos().y);

		return add(newEntity, false);
	}

	protected Entity addDistinct(String textureName, int x, int y) {
		updateLevelRect(x, y);

		return add(textureName, x, y, true);
	}

	private static final int EXPECTED_MAX_ENTITY_SIZE = 256;

	private void updateLevelRect(float x, float y) {
		if (levelRect.getWidth() < x + EXPECTED_MAX_ENTITY_SIZE) {
			levelRect.setWidth(x + EXPECTED_MAX_ENTITY_SIZE);
		}

		if (levelRect.getX() > x - EXPECTED_MAX_ENTITY_SIZE) {
			float expansion = Math.abs(levelRect.getX() - (x - EXPECTED_MAX_ENTITY_SIZE));
			levelRect.setX(x - EXPECTED_MAX_ENTITY_SIZE);
			levelRect.setWidth(levelRect.getWidth() + expansion);
		}

		if (levelRect.getWidth() < y + EXPECTED_MAX_ENTITY_SIZE) {
			levelRect.setWidth(y + EXPECTED_MAX_ENTITY_SIZE);
		}

		if (levelRect.getY() > y - EXPECTED_MAX_ENTITY_SIZE) {
			float expansion = Math.abs(levelRect.getY() - (y - EXPECTED_MAX_ENTITY_SIZE));
			levelRect.setY(y - EXPECTED_MAX_ENTITY_SIZE);
			levelRect.setHeight(levelRect.getHeight() + expansion);
		}
	}

	protected Entity add(Class<?> entityClass, float x, float y, int grid) {
		updateLevelRect(x, y);

		return add(EntityCreator.create(entityClass, new Vector2f(x, y), grid, false));
	}

	protected Entity add(String textureName, float x, float y, int grid) {
		updateLevelRect(x, y);

		return add(EntityCreator.create(textureName, new Vector2f(x, y), grid), false);
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
			if (dEntity.getCollisionShape().intersects(entity.getCollisionShape())) {
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

		int viewHeight = Display.getHeight();
		int viewWidth = Display.getWidth();

		// draw vertical edges
		for (int i = -viewHeight; i <= maxY + viewHeight; i += borderBlockSize) {
			Vector2f posT = new Vector2f(-borderBlockSize, i);
			Vector2f posB = new Vector2f(maxX, i);
			for (int j = 0; j <= viewHeight; j += borderBlockSize) {
				Vector2f posTadj = new Vector2f(posT.x - j, posT.y);
				Vector2f posBadj = new Vector2f(posB.x + j, posB.y);

				newEntities.add(EntityCreator.create(borderTexture, posTadj, borderBlockSize, false));
				newEntities.add(EntityCreator.create(borderTexture, posBadj, borderBlockSize, false));
			}
		}

		// draw horizontal edges
		for (int i = -viewWidth; i <= maxX + viewWidth; i += borderBlockSize) {

			Vector2f posL = new Vector2f(i, -borderBlockSize);
			Vector2f posR = new Vector2f(i, maxY);

			for (int j = 0; j <= viewWidth; j += borderBlockSize) {
				Vector2f posLadj = new Vector2f(posL.x, posL.y - j);
				Vector2f posRadj = new Vector2f(posR.x, posR.y + j);

				newEntities.add(EntityCreator.create(borderTexture, posLadj, borderBlockSize, false));
				newEntities.add(EntityCreator.create(borderTexture, posRadj, borderBlockSize, false));
			}
		}

		for (Entity newEntity : newEntities) {
			add(newEntity);
		}

	}

}
