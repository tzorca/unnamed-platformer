package game;

import game.entities.ActiveEntity;
import game.entities.Entity;
import game.logic.CollisionProcessor;
import game.parameters.Ref;
import game.parameters.Ref.BlueprintComponent;
import game.parameters.Ref.Flag;
import game.structures.Blueprint;
import game.structures.Graphic;
import game.structures.QuadTree;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.SerializationUtils;

import app.App;
import app.App.State;
import app.ViewManager;

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
		System.out.println("Level Rect = " + rect);
	}

	public Rectangle getRect() {
		return new Rectangle(0, 0, rect.width, rect.height);
	}

	public Level(LinkedList<Entity> origEntities, Rectangle levelRect) {
		resetTo(origEntities);
		setRect(levelRect);
	}

	public Level(LinkedList<Entity> origEntities) {
		resetTo(origEntities);
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
			System.out.println("You passed in a null blueprint!");
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
		entities = SerializationUtils.clone(srcEntities);
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

	public List<Entity> getEntities() {
		return this.entities;
	}

	public Entity getTopmostEntity(Point pos) {
		ListIterator<Entity> rI = entities.listIterator(entities.size());

		while (rI.hasPrevious()) {
			Entity e = rI.previous();
			if (e.getBox().contains(pos)) {
				return e;
			}
		}
		return null;
	}

	public void update(long timeDelta) {

		quadTree.clear();

		// perform entity logic
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (App.state == State.play) {
				entity.update(timeDelta);

			}

			if (entity.checkFlag(Flag.outOfPlay)) {
				entityIterator.remove();
			} else if (App.state == State.play) {
				quadTree.insert(entity);
			}
		}

		if (App.state == State.play) {
			List<Entity> entitiesToCheck = new ArrayList<Entity>();
			for (Entity a : entities) {
				if (a instanceof ActiveEntity) {

					entitiesToCheck.clear();
					quadTree.retrieve(entitiesToCheck, a.getBox());

					// if (a.checkFlag(Flag.player)) {
					// for (Entity b : entitiesToCheck) {
					// b.graphic.setTempHighlight();
					// }
					// }

					CollisionProcessor.processMove(a, entitiesToCheck);

					if (a.checkFlag(Flag.player)) {
						ViewManager.centerCamera(a.getCenter());
					}
				}
			}
		}

		// add new entities
		materializeNewEntities();
	}

	public Entity findEntityByFlag(Flag flag) {
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (entity.checkFlag(flag)) {
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

}
