package model;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import model.entities.Entity;
import model.parameters.ContentRef.ContentType;
import model.parameters.Ref.BlueprintComponent;
import model.structures.Blueprint;
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

		String filename = ContentManager.getFilename(ContentType.game, name);
		return toBlueprint().save(filename);
	}

	public static Game load(String name) {
		String filename = ContentManager.getFilename(ContentType.game, name);
		return fromBlueprint(Blueprint.load(filename));
	}

	public Blueprint toBlueprint() {
		Blueprint gBP = new Blueprint();

		List<Blueprint> lBPs = new LinkedList<Blueprint>();
		for (Level lvl : levels) {
			lBPs.add(lvl.toBlueprint());
		}

		gBP.put(BlueprintComponent.levels, lBPs);
		gBP.put(BlueprintComponent.gameName, name);

		return gBP;
	}

	@SuppressWarnings("unchecked")
	private static Game fromBlueprint(Blueprint bp) {
		if (bp == null) {
			System.out.println("You passed in a null blueprint!");
			return null;
		}

		List<Blueprint> lBPs = (LinkedList<Blueprint>) bp
				.get(BlueprintComponent.levels);

		String newGameName = (String) bp.get(BlueprintComponent.gameName);

		Game newGame = new Game(newGameName, false);

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

	public void update(long timeDelta) {
		level.update(timeDelta);
	}

	public List<Entity> getEntities() {
		return level.getEntities();
	}

	public void draw() {
		ViewManager.clear(level.bgColor);
		ViewManager.drawBG(level.bgTexture);

		if (App.state == State.edit) {
			ViewManager.drawEditGrid(level.gridSize);
		}

		for (Entity entity : level.getEntities()) {
			ViewManager.drawEntity(entity);
		}

		if (App.state == State.edit) {
			GUI_Edit.drawPlaceholderElement();
		}
	}

	public void addEntity(Entity e, boolean original) {
		level.addEntity(e, original);
	}

	public int getLevelIndex() {
		return levelIndex;
	}

	public Rectangle getLevelRect() {
		return level.rect;
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
