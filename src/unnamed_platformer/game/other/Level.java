package unnamed_platformer.game.other;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.view.Graphic;
import unnamed_platformer.view.ViewManager;

public class Level
{
	private transient Graphic bgGraphic = new Graphic("bg", new Color(0x27,
			0x27, 0x41, 1));

	private transient LinkedList<Entity> entities = new LinkedList<Entity>();
	private transient LinkedList<Entity> newEntities = new LinkedList<Entity>();
	private LinkedList<EntitySetup> entitySetups = new LinkedList<EntitySetup>();

	private transient Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	private transient ActiveEntity playerEntity;

	private void setRect(final Rectangle rect) {
		this.rect = rect;
	}

	public Rectangle getRect() {
		return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(),
				rect.getHeight());
	}

	public Level(LinkedList<Entity> origEntities, Rectangle levelRect) {
		init(origEntities, levelRect);
	}

	public Level(LinkedList<Entity> origEntities) {
		init(origEntities, Ref.DEFAULT_LEVEL_RECTANGLE);
	}

	private void init(LinkedList<Entity> origEntities, Rectangle levelRect) {
		resetTo(EntityCreator.getSetupCollection(origEntities));
		setRect(levelRect);
	}

	private void setupPlayer() {
		for (Entity e : entities) {
			if (e.isFlagSet(Flag.PLAYER)) {
				playerEntity = (ActiveEntity) e;

				ViewManager.centerCamera(playerEntity.getCenter(), rect);
			}
		}
	}

	public Blueprint toBlueprint() {
		Blueprint lBP = new Blueprint();

		lBP.put(BlueprintField.LEVEL_BG, bgGraphic);
		lBP.put(BlueprintField.LEVEL_RECT, getRect());
		lBP.put(BlueprintField.LEVEL_ENTITIES, entitySetups);
		return lBP;
	}

	@SuppressWarnings("unchecked")
	static Level fromBlueprint(final Blueprint levelBlueprint) {
		if (levelBlueprint == null) {
			System.out
					.println("Error: Can't create a level from a null blueprint.");
			return null;
		}

		LinkedList<EntitySetup> entitySetups = (LinkedList<EntitySetup>) levelBlueprint
				.get(BlueprintField.LEVEL_ENTITIES);

		Level newLevel = new Level(
				EntityCreator.buildFromSetupCollection(entitySetups));
		newLevel.bgGraphic = (Graphic) levelBlueprint
				.get(BlueprintField.LEVEL_BG);
		newLevel.setRect((Rectangle) levelBlueprint
				.get(BlueprintField.LEVEL_RECT));

		return newLevel;
	}

	public void resetToOriginal() {
		resetTo(entitySetups);
	}

	private void resetTo(LinkedList<EntitySetup> setups) {
		// get rid of old pointer to player entity (no longer valid)
		playerEntity = null;

		entitySetups = setups;
		entities = EntityCreator.buildFromSetupCollection(entitySetups);
	}

	public void materializeNewEntities() {
		entities.addAll(newEntities);
		newEntities.clear();
	}

	public void addEntity(final Entity entity) {
		newEntities.add(entity);
	}

	public void clear() {
		if (entities != null) {
			entities.clear();
		}
		entities = new LinkedList<Entity>();
	}

	public LinkedList<Entity> getEntities() {
		return this.entities;
	}

	public Entity getTopmostEntity(Vector2f point, int range) {
		ListIterator<Entity> entityIterator = entities.listIterator(entities
				.size());

		Rectangle rangeBox = new Rectangle(point.x - range, point.y - range,
				range * 2, range * 2);

		while (entityIterator.hasPrevious()) {
			Entity entity = entityIterator.previous();
			if (entity.getCollisionShape().intersects(rangeBox)) {
				return entity;
			}
		}
		return null;
	}

	public void update() {

		// find player (if not already found)
		if (playerEntity == null) {
			setupPlayer();
		}

		// set start time if not yet set
		if (startTime == null && playerEntity != null) {
			startTime = TimeManager.time();
		}

		Iterator<Entity> entityIterator = entities.iterator();

		SpatialHash.clear();

		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			// don't do logic on entities outside the view
			if (!ViewManager.rectInView(entity.getOriginalBox())) {
				continue;
			}

			if (World.playing()) {

				// perform entity logic
				Vector2f pos = entity.getPos();
				entity.update();

				// if no spatial hash exists or position changed, update spatial
				// hash
				boolean posChanged = !pos.equals(entity.getPos());
				if (!SpatialHash.has(entity) || posChanged) {
					SpatialHash.insert(entity);
				}

				if (entity.isFlagSet(Flag.PLAYER)) {
					playerEntity = (ActiveEntity) entity;

				}
			}

			// Don't do logic on entities that are temporarily inactive
			if (entity.isFlagSet(Flag.INACTIVE_UNTIL_PLAYER_DEATH)) {
				SpatialHash.remove(entity);
				continue;
			}

			// remove entities that have been flagged to be removed
			if (entity.isFlagSet(Flag.OUT_OF_PLAY)) {
				entityIterator.remove();
				SpatialHash.remove(entity);
				continue;
			}
		}

		if (World.playing()) {
			PhysicsProcessor.processInteractionsNearRegisteredEntities();

			if (playerEntity != null) {
				ViewManager.centerCamera(playerEntity.getCenter(), rect);
			}
		}

		// add new entities
		materializeNewEntities();
	}

	public Entity findEntityByFlag(Flag flag) {
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();
			if (entity.isFlagSet(flag)) {
				return entity;
			}
		}
		return null;
	}

	public void removeEntity(final Entity entity) {
		entity.setFlag(Flag.OUT_OF_PLAY, true);
	}

	public void resetToCurrent() {
		resetTo(EntityCreator.getSetupCollection(entities));
	}

	public Graphic getBackgroundGraphic() {
		return bgGraphic;
	}

	public void setSize(Rectangle newRect) {
		this.rect = newRect;
	}

	public void drawBackground() {
		if (bgGraphic.hasTextureName()) {
			ViewManager.drawBG(bgGraphic.getTexture());
		}
	}

	TreeMap<Integer, List<Entity>> zIndexBuckets = new TreeMap<Integer, List<Entity>>();

	public void drawForeground() {

		// clear previous buckets
		for (List<Entity> entities : zIndexBuckets.values()) {
			entities.clear();
		}

		for (final Entity entity : entities) {
			if (!zIndexBuckets.containsKey(entity.zIndex)) {
				zIndexBuckets.put(entity.zIndex, new LinkedList<Entity>());
			}
			zIndexBuckets.get(entity.zIndex).add(entity);
		}

		for (final int zIndex : zIndexBuckets.keySet()) {
			for (final Entity entity : zIndexBuckets.get(zIndex)) {
				entity.draw();
			}
		}

	}

	public ActiveEntity getPlayer() {
		if (playerEntity == null) {
			setupPlayer();
		}
		return playerEntity;
	}

	Long startTime = null;

	public int getElapsedSeconds() {
		if (startTime == null) {
			return 0;
		}

		return (int) TimeManager.secondsSince(startTime);
	}

	public void signalPlayerDeath() {
		for (Entity e : entities) {
			if (e.isFlagSet(Flag.INACTIVE_UNTIL_PLAYER_DEATH)) {
				e.setFlag(Flag.INACTIVE_UNTIL_PLAYER_DEATH, false);
				e.setFlag(Flag.INVISIBLE, false);
			}
		}

	}
}
