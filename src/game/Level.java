package game;

import game.entities.Entity;
import game.parameters.Ref;
import game.parameters.ContentRef.ContentType;
import game.parameters.Ref.BlueprintComponent;
import game.parameters.Ref.Flag;
import game.structures.Blueprint;
import game.structures.FlColor;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.SerializationUtils;
import org.newdawn.slick.opengl.Texture;

import app.App;
import app.App.State;
import app.ContentManager;
import app.ViewManager;

public class Level {
	public boolean bgStretch = false;
	public FlColor bgColor = Ref.DEFAULT_BG_COLOR;
	public String bgTexName;
	public Texture bgTexture;

	public int gridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

	private LinkedList<Entity> entities = new LinkedList<Entity>(),
			originalEntities = new LinkedList<Entity>(),
			newEntities = new LinkedList<Entity>();

	public Rectangle rect = Ref.DEFAULT_LEVEL_RECTANGLE;
	public boolean won = false;

	public Level(LinkedList<Entity> origEntities) {
		resetTo(origEntities);
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
		newLevel.setBGTexture((String) lBP
				.get(BlueprintComponent.levelBackgroundTexture));
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
		materializeNewEntities();
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

	public void removeEntity(Entity e) {
		e.setFlag(Flag.outOfPlay, true);
	}

	public void resetToCurrent() {
		resetTo(entities);
	}

}
