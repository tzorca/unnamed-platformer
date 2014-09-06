package unnamed_platformer.game.other;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.view.Graphic;
import unnamed_platformer.view.ViewManager;

public class Level
{
	// private boolean bgStretch = false;
	private Graphic bgGraphic = new Graphic("bg",
			new Color(0x27, 0x27, 0x41, 1));

	public int gridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private LinkedList<Entity> entities = new LinkedList<Entity>(),
			newEntities = new LinkedList<Entity>();
	private LinkedList<EntitySetup> entitySetups = new LinkedList<EntitySetup>();

	private Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	private transient QuadTree quadTree = new QuadTree(0,
			Ref.DEFAULT_LEVEL_RECTANGLE);
	private Entity playerEntity;

	private void setRect(final Rectangle rect) {
		quadTree = new QuadTree(0, rect);
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
				playerEntity = e;

				ViewManager.centerCamera(playerEntity.getCenter());
			}
		}
	}

	public void save(final String filename) {
		toBlueprint().save(filename);
	}

	public static Level load(final String filename) {
		return fromBlueprint(Blueprint.load(filename, false));
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
		entitySetups = setups;
		entities = EntityCreator.buildFromSetupCollection(entitySetups);
		onStart();
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

	public Entity getTopmostEntity(Vector2f point) {
		ListIterator<Entity> entityIterator = entities.listIterator(entities
				.size());

		while (entityIterator.hasPrevious()) {
			Entity entity = entityIterator.previous();
			if (entity.getCollisionShape().contains(point.x, point.y)) {
				return entity;
			}
		}
		return null;
	}

	// perform entity logic and update quadtree
	public void update() {

		// find player (if not already found)
		if (playerEntity == null) {
			setupPlayer();
		}

		// clear previous update's quad tree
		quadTree.clear();

		Iterator<Entity> entityIterator = entities.iterator();

		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			// don't do logic on entities outside the view
			if (!ViewManager.rectInView(entity.getOriginalBox())) {
				continue;
			}

			if (World.playing()) {
				// perform entity logic
				entity.update();

				if (entity.isFlagSet(Flag.PLAYER)) {
					playerEntity = entity;

				}
			}

			// remove entities that have been flagged to be removed
			if (entity.isFlagSet(Flag.OUT_OF_PLAY)) {
				entityIterator.remove();
				continue;
			}

			// add existing entities to quadtree
			if (World.playing()) {
				quadTree.insert(entity,
						QuadTree.increaseRect(entity.getCollisionRect()));
			}
		}

		if (World.playing()) {
			PhysicsProcessor.checkForInteractionsWithRegisteredEntities();

			if (playerEntity != null) {
				ViewManager.centerCamera(playerEntity.getCenter());
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

	public List<Entity> retrieveFromQuadTree(List<Entity> entities,
			Rectangle box) {
		return quadTree.retrieve(entities, box);
	}

	private void onStart() {
		// // take screenshot on start of level 1
		// if (World.currentGame == null ||
		// World.currentGame.getLevelIndex() != 0
		// || App.state != State.Play) {
		// return;
		// }
		//
		// try {
		// SwingUtilities.invokeLater(new Runnable(){
		//
		// @Override
		// public void run() {
		// BufferedImage screenshot = ViewManager.getScreenshot();
		// ImageIcon serializablePreviewImage = new ImageIcon(screenshot);
		// if (screenshot != null) {
		// World.currentGame.setPreviewImage(serializablePreviewImage);
		// }
		//
		// }});
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
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

	public void drawForeground() {
		TreeMap<Integer, List<Entity>> zIndexBuckets = new TreeMap<Integer, List<Entity>>();

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
}
