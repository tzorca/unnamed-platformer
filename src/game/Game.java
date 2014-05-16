package game;

import game.entities.Entity;
import game.parameters.ContentRef.ContentType;
import game.parameters.Ref.BlueprintComponent;
import game.structures.Blueprint;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import app.App;
import app.App.State;
import app.ContentManager;
import app.ViewManager;
import app.gui.GUI_Edit;

public class Game {
	private List<Level> levels = new LinkedList<Level>();
	private Level level; // current Level
	private int levelIndex = 0;
	private String name;

	public Game(String name, boolean addBlank) {
		this.name = name;
		if (addBlank) {
			addBlankLevel();
		}
		setCurrentLevel(0);
	}

	public boolean save(String name) {
		this.name = name;
		String filename = ContentManager.getFilename(ContentType.game, name);
		return toBlueprint().save(filename);
	}

	public static Game load(String name) {
		String filename = ContentManager.getFilename(ContentType.game, name);
		return fromBlueprint(Blueprint.load(filename), name);
	}

	public Blueprint toBlueprint() {
		Blueprint gBP = new Blueprint();

		List<Blueprint> lBPs = new LinkedList<Blueprint>();
		for (Level lvl : levels) {
			lBPs.add(lvl.toBlueprint());
		}

		gBP.put(BlueprintComponent.levels, lBPs);

		return gBP;
	}

	@SuppressWarnings("unchecked")
	private static Game fromBlueprint(Blueprint bp, String name) {
		if (bp == null) {
			System.out.println("You passed in a null blueprint!");
			return null;
		}

		List<Blueprint> lBPs = (LinkedList<Blueprint>) bp
				.get(BlueprintComponent.levels);

		Game newGame = new Game(name, false);

		for (Blueprint lvlBlueprint : lBPs) {
			newGame.levels.add(Level.fromBlueprint(lvlBlueprint));
		}

		newGame.setCurrentLevel(0);
		return newGame;
	}

	public boolean hasLevel(int destination) {
		return levels.size() > destination && destination >= 0;
	}

	public void setCurrentLevel(int destination) {
		if (hasLevel(destination)) {
			level = levels.get(destination);
			levelIndex = destination;
		} else {
			// System.out.println("Level with index " + destination
			// + " doesn't exist.");
			if (level != null) {
				level.won = true;
			}
			return;
		}
	}

	public void update() {
		level.update();
	}

	public List<Entity> getEntities() {
		return level.getEntities();
	}

	public void draw() {
		ViewManager.clear(level.bgGraphic.color);
		ViewManager.drawBG(level.bgGraphic.getTexture());

		if (App.state == State.edit) {
			ViewManager.drawEditGrid(level.gridSize);
		}

		for (Entity entity : level.getEntities()) {
			ViewManager.drawEntity(entity);
		}

		if (App.state == State.edit) {
			GUI_Edit.drawEntityPlaceholder();
		}
	}

	public void addEntity(Entity e) {
		level.addEntity(e);
	}

	public int getLevelIndex() {
		return levelIndex;
	}

	public Rectangle getLevelRect() {
		return level.getRect();
	}

	public void addPremadeLevel(Level lvl) {
		levels.add(lvl);
	}

	public Level getLevel() {
		return level;
	}

	public void addBlankLevel() {

		levels.add(new Level(new LinkedList<Entity>()));
	}

	public int getLevelCount() {
		return levels.size();
	}

	public void removeLevel(int index) {
		if (this.hasLevel(index)) {
			levels.remove(index);
		} else {
			System.out.println("Can't delete level " + index
					+ " (it doesn't exist)");
		}
	}

	public String getName() {
		return this.name;
	}

	public void replaceCurrentLevel(Level lvl) {
		level = lvl;
		levels.set(levelIndex, lvl);
	}

}
