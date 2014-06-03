package unnamed_platformer.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.app.App;
import unnamed_platformer.app.App.State;
import unnamed_platformer.app.ContentManager;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.logic.Editor;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
import unnamed_platformer.game.parameters.Ref.BlueprintField;
import unnamed_platformer.game.structures.Blueprint;
import unnamed_platformer.gui.GUI_Edit;

public class Game {
	private List<Level> levels = new LinkedList<Level>();
	private Level level; // current Level
	private int levelIndex = 0;
	private String name;
	private ImageIcon previewImage = null;

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
		return fromBlueprint(Blueprint.load(filename, false), name);
	}

	public Blueprint toBlueprint() {
		Blueprint gBP = new Blueprint();


		gBP.put(BlueprintField.previewImage, previewImage);
		
		List<Blueprint> lBPs = new LinkedList<Blueprint>();
		for (Level lvl : levels) {
			lBPs.add(lvl.toBlueprint());
		}

		gBP.put(BlueprintField.levels, lBPs);

		return gBP;
	}

	@SuppressWarnings("unchecked")
	private static Game fromBlueprint(Blueprint bp, String name) {
		if (bp == null) {
			App.print("Currently existing blueprint is invalid. Creating new game...");
			return new Game(name, true);
		}

		List<Blueprint> lBPs = (LinkedList<Blueprint>) bp
				.get(BlueprintField.levels);

		Game newGame = new Game(name, false);

		for (Blueprint lvlBlueprint : lBPs) {
			newGame.levels.add(Level.fromBlueprint(lvlBlueprint));
		}

		newGame.setCurrentLevel(0);
		return newGame;
	}

	public static Image getPreviewImage(Blueprint bp) {

		if (bp == null || !bp.containsKey(BlueprintField.previewImage)) {

			return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		}
		return ((ImageIcon) bp.get(BlueprintField.previewImage)).getImage();
	}

	public boolean hasLevel(int destination) {
		return levels.size() > destination && destination >= 0;
	}

	public void setCurrentLevel(int destination) {
		if (hasLevel(destination)) {
			level = levels.get(destination);
			levelIndex = destination;
		} else {
			// App.print("Level with index " + destination
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

		if (App.state == State.Edit) {
			ViewManager.drawEditorGrid(Editor.gridSize);
		}

		ViewManager.drawEntities(level.getEntities());

		if (App.state == State.Edit) {
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
			App.print("Can't delete level " + index + " (it doesn't exist)");
		}
	}

	public String getName() {
		return this.name;
	}

	public void replaceCurrentLevel(Level lvl) {
		level = lvl;
		levels.set(levelIndex, lvl);
	}

	public void setPreviewImage(
			ImageIcon previewImage) {
		this.previewImage = previewImage;
	}

}
