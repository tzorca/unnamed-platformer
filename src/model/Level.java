package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import model.Ref.BlueprintComponent;
import model.Ref.Flag;
import model.entities.Entity;
import model.structures.Blueprint;
import model.structures.FlColor;

import org.newdawn.slick.opengl.Texture;

import app.App;
import app.App.State;
import app.ContentManager;
import app.ContentManager.ContentType;
import app.ViewManager;

public class Level {

	public boolean bgStretch = false;
	public FlColor bgColor = new FlColor(176f/255f,196f/255f,222/255f);
	public String bgTexName;
	public Texture bgTexture;

	public int gridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private List<Entity> entities = (Collections
			.synchronizedList(new LinkedList<Entity>()));

	private List<Entity> newEntities = new ArrayList<Entity>();

	public Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	public boolean won = false;

	public Level(List<Entity> list) {
		loadStartupEntities(list);
		setupLevel();
	}

	public void setupLevel() {
		setBGTexture("sky");
	}

	private void setBGTexture(String textureName) {
		this.bgTexName = textureName;
		this.bgTexture = (Texture) ContentManager.get(ContentType.texture,
				bgTexName);
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

		lBP.put(BlueprintComponent.levelBackgroundTexture, bgTexName);
		lBP.put(BlueprintComponent.levelRect, rect);
		lBP.put(BlueprintComponent.levelEntities, entities);

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
		Level newLevel = new Level(
				(List<Entity>) lBP.get(BlueprintComponent.levelEntities));
		newLevel.setBGTexture((String) lBP
				.get(BlueprintComponent.levelBackgroundTexture));
		newLevel.setRect((Rectangle) lBP.get(BlueprintComponent.levelRect));
		newLevel.setupLevel();

		return newLevel;
	}

	public void loadStartupEntities(List<Entity> nE) {
		for (Entity e : nE) {
			if (e.getTexture() == null) {
				e.setTexture(e.getTextureName());
			}
		}

		entities.clear();
		entities.addAll(nE);
	}

	public void flushNewEntities() {
		entities.addAll(newEntities);
		newEntities.clear();
	}

	private void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public void addEntity(Entity e) {
		newEntities.add(e);
	}


	public void clear() {
		if (entities != null) {
			entities.clear();
		}
		entities = (Collections.synchronizedList(new LinkedList<Entity>()));
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

	public void update(float timeDelta) {

		// perform entity logic
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (App.state == State.play) {
				entity.update(timeDelta);

				if (entity.checkFlag(Flag.player)) {
					ViewManager.centerCamera(entity.getCenter());
				}
			}

			if (entity.checkFlag(Flag.outOfPlay)) {
				entityIterator.remove();
			}
		}

		// add new entities
		flushNewEntities();
	}

	public Rectangle getRect() {
		return new Rectangle(0, 0, rect.width, rect.height);
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

}
