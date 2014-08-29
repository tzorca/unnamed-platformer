package unnamed_platformer.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

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
	private Graphic bgGraphic = new Graphic("bg", new Color(0x27, 0x27, 0x41, 1));

	public int gridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private LinkedList<Entity> entities = new LinkedList<Entity>(),
			newEntities = new LinkedList<Entity>();
	private LinkedList<EntitySetup> entitySetups = new LinkedList<EntitySetup>();

	private Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	private transient QuadTree quadTree = new QuadTree(0,
			Ref.DEFAULT_LEVEL_RECTANGLE);

	private void setRect(Rectangle rect) {
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
		setupPlayer();
	}

	Entity playerEntity;

	private void setupPlayer() {
		for (Entity e : entities) {
			if (e.isFlagSet(Flag.PLAYER)) {
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

		lBP.put(BlueprintField.LEVEL_BG, bgGraphic);
		lBP.put(BlueprintField.LEVEL_RECT, getRect());
		lBP.put(BlueprintField.LEVEL_ENTITIES, entitySetups);

		return lBP;
	}

	@SuppressWarnings("unchecked")
	static Level fromBlueprint(Blueprint lBP) {
		if (lBP == null) {
			System.out
					.println("Error: Can't create a level from a null blueprint.");
			return null;
		}

		LinkedList<EntitySetup> entitySetups = (LinkedList<EntitySetup>) lBP
				.get(BlueprintField.LEVEL_ENTITIES);

		Level newLevel = new Level(
				EntityCreator.buildFromSetupCollection(entitySetups));
		newLevel.bgGraphic = (Graphic) lBP.get(BlueprintField.LEVEL_BG);
		newLevel.setRect((Rectangle) lBP.get(BlueprintField.LEVEL_RECT));

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

	// perform entity logic and update quadtree
	public void update() {

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

	public void removeEntity(Entity e) {
		e.setFlag(Flag.OUT_OF_PLAY, true);
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

}
