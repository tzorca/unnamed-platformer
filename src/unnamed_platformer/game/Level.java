package unnamed_platformer.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.App;
import unnamed_platformer.app.CloneManager;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.app.App.State;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.logic.PhysicsProcessor;
import unnamed_platformer.game.parameters.Ref;
import unnamed_platformer.game.parameters.Ref.BlueprintComponent;
import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.structures.Blueprint;
import unnamed_platformer.game.structures.Graphic;
import unnamed_platformer.game.structures.QuadTree;

public class Level {
	public boolean bgStretch = false;
	public Graphic bgGraphic = new Graphic();

	public int gridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private LinkedList<Entity> entities = new LinkedList<Entity>(),
			originalEntities = new LinkedList<Entity>(),
			newEntities = new LinkedList<Entity>();

	private Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	private transient QuadTree quadTree = new QuadTree(0,
			Ref.DEFAULT_LEVEL_RECTANGLE);

	public boolean won = false;

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
		resetTo(origEntities);
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
			App.print("Warning: Player entity not found.");
		}
	}

	public void save(String filename) {
		toBlueprint().save(filename);
	}

	public static Level load(String filename) {
		return fromBlueprint(Blueprint.load(filename));
	}

	public Blueprint toBlueprint() {
		Blueprint lBP = new Blueprint();

		updateStartPositions();

		lBP.put(BlueprintComponent.levelBG, bgGraphic);
		lBP.put(BlueprintComponent.levelRect, getRect());
		lBP.put(BlueprintComponent.levelEntities, originalEntities);

		return lBP;
	}

	public void updateStartPositions() {
		for (Entity e : entities) {
			e.setStartPos(e.getPos());
		}

	}

	@SuppressWarnings("unchecked")
	static Level fromBlueprint(Blueprint lBP) {
		if (lBP == null) {
			App.print("You passed in a null blueprint!");
			return null;
		}
		Level newLevel = new Level(new LinkedList<Entity>(
				(List<Entity>) lBP.get(BlueprintComponent.levelEntities)));
		newLevel.bgGraphic = (Graphic) lBP.get(BlueprintComponent.levelBG);
		newLevel.setRect((Rectangle) lBP.get(BlueprintComponent.levelRect));

		return newLevel;
	}

	public void resetToOriginal() {
		resetTo(originalEntities);
	}

	private void resetTo(LinkedList<Entity> srcEntities) {
		originalEntities = srcEntities;
		entities = CloneManager.deepClone(srcEntities);
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
			if (e.getBox().contains(point.x, point.y)) {
				return e;
			}
		}
		return null;
	}

	public void update() {

		// clear previous update's quad tree
		quadTree.clear();

		// grab viewRect for use in loop
		Rectangle viewRect = ViewManager.getViewRect();

		// perform entity logic and update quadtree
		// only for entities in the player's current view
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (!entity.getBox().intersects(viewRect)) {
				continue;
			}

			if (App.state == State.play) {
				// perform entity logic
				entity.update();

				if (entity.isFlagSet(Flag.player)) {
					playerEntity = entity;
				}
			}

			// remove entities that have been flagged to be removed
			// unless they are the player entity
			if (entity.isFlagSet(Flag.outOfPlay)) {

				if (entity.isFlagSet(Flag.player) && App.state != State.edit) {
					continue;
				}
				entityIterator.remove();
				continue;
			}

			// add existing entities to quadtree
			if (App.state == State.play) {
				quadTree.insert(entity);
			}
		}

		if (App.state == State.play) {
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
		resetTo(entities);
	}

	public List<Entity> retrieveFromQuadTree(List<Entity> entities,
			Rectangle box) {
		return quadTree.retrieve(entities, box);
	}

}
