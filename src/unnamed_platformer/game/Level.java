package unnamed_platformer.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.GameStateManager;
import unnamed_platformer.app.GameStateManager.State;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.structures.Blueprint;
import unnamed_platformer.structures.Graphic;
import unnamed_platformer.structures.QuadTree;

public class Level {
	// private boolean bgStretch = false;
	private Graphic bgGraphic = new Graphic(new Color(0xF8, 0xF8, 0xFF, 1));

	public int gridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private LinkedList<Entity> entities = new LinkedList<Entity>(), newEntities = new LinkedList<Entity>();
	private LinkedList<EntitySetup> entitySetups = new LinkedList<EntitySetup>();

	private Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	private transient QuadTree quadTree = new QuadTree(0, Ref.DEFAULT_LEVEL_RECTANGLE);

	private void setRect(Rectangle rect) {
		quadTree = new QuadTree(0, rect);
		this.rect = rect;
	}

	public Rectangle getRect() {
		return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
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
		setupPlayer();
	}

	Entity playerEntity;

	private void setupPlayer() {
		for (Entity e : entities) {
			if (e.isFlagSet(Flag.player)) {
				playerEntity = e;

				ViewManager.centerCamera(playerEntity.getCenter());
			}
		}
		if (playerEntity == null) {
			System.out.println("Warning: Player entity not found.");
		}
	}

	public void save(String filename) {
		toBlueprint().save(filename);
	}

	public static Level load(String filename) {
		return fromBlueprint(Blueprint.load(filename, false));
	}

	public Blueprint toBlueprint() {
		Blueprint lBP = new Blueprint();

		lBP.put(BlueprintField.levelBG, bgGraphic);
		lBP.put(BlueprintField.levelRect, getRect());
		lBP.put(BlueprintField.levelEntities, entitySetups);

		return lBP;
	}

	@SuppressWarnings("unchecked")
	static Level fromBlueprint(Blueprint lBP) {
		if (lBP == null) {
			System.out.println("Error: Can't create a level from a null blueprint.");
			return null;
		}

		LinkedList<EntitySetup> entitySetups = (LinkedList<EntitySetup>) lBP.get(BlueprintField.levelEntities);

		Level newLevel = new Level(EntityCreator.buildFromSetupCollection(entitySetups));
		newLevel.bgGraphic = (Graphic) lBP.get(BlueprintField.levelBG);
		newLevel.setRect((Rectangle) lBP.get(BlueprintField.levelRect));

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

	public void addEntity(Entity e) {
		newEntities.add(e);
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
		ListIterator<Entity> rI = entities.listIterator(entities.size());

		while (rI.hasPrevious()) {
			Entity e = rI.previous();
			if (e.getCollisionShape().contains(point.x, point.y)) {
				return e;
			}
		}
		return null;
	}

	public void update() {

		// clear previous update's quad tree
		quadTree.clear();

		// perform entity logic and update quadtree
		// only for entities in the player's current view
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (!ViewManager.rectInView(entity.getOriginalBox())) {
				continue;
			}

			if (GameStateManager.at(State.Play)) {
				// perform entity logic
				entity.update();

				if (entity.isFlagSet(Flag.player)) {
					playerEntity = entity;

				}
			}

			// remove entities that have been flagged to be removed
			// unless they are the player entity
			if (entity.isFlagSet(Flag.outOfPlay)) {

				if (entity.isFlagSet(Flag.player) && !GameStateManager.at( State.Edit)) {
					continue;
				}
				entityIterator.remove();
				continue;
			}

			// add existing entities to quadtree
			if (GameStateManager.at(State.Play)) {
				quadTree.insert(entity, QuadTree.increaseRect(entity.getCollisionRect()));
			}
		}

		if (GameStateManager.at(State.Play)) {
			PhysicsProcessor.processMoves();

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

	public void removeEntity(Entity e) {
		e.setFlag(Flag.outOfPlay, true);
	}

	public void resetToCurrent() {
		resetTo(EntityCreator.getSetupCollection(entities));
	}

	public List<Entity> retrieveFromQuadTree(List<Entity> entities, Rectangle box) {
		return quadTree.retrieve(entities, box);
	}

	private void onStart() {
		// // take screenshot on start of level 1
		// if (GameManager.currentGame == null ||
		// GameManager.currentGame.getLevelIndex() != 0
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
		// GameManager.currentGame.setPreviewImage(serializablePreviewImage);
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

}
