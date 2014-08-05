package unnamed_platformer.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.app.GameStateManager;
import unnamed_platformer.app.GameStateManager.State;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.Screen;
import unnamed_platformer.gui.Screen_Edit;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.Blueprint;
import unnamed_platformer.structures.Graphic;

public class World {
	private static List<Level> levels = new LinkedList<Level>();
	private static Level level; // current Level
	private static int levelIndex = 0;
	private static String _name;
	private static ImageIcon previewImage = null;

	public static void reset(String name, boolean addBlank) {
		_name = name;
		levels.clear();
		if (addBlank) {
			addBlankLevel();
		}
		setCurrentLevel(0);
	}

	public static boolean save(String name) {
		_name = name;
		String filename = ResManager.getFilename(World.class, name);
		return toBlueprint().save(filename);
	}

	public static void load(String name) {
		reset(name, false);
		String filename = ResManager.getFilename(World.class, name);
		fromBlueprint(Blueprint.load(filename, false), name);
	}

	public static Blueprint toBlueprint() {
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
	private static void fromBlueprint(Blueprint bp, String name) {
		if (bp == null) {
			System.out.println("Currently existing blueprint is invalid. Creating new game...");
			reset(name, true);
			return;
		}

		List<Blueprint> lBPs = (LinkedList<Blueprint>) bp.get(BlueprintField.levels);

		reset(name, false);

		for (Blueprint lvlBlueprint : lBPs) {
			levels.add(Level.fromBlueprint(lvlBlueprint));
		}

		setCurrentLevel(0);
	}

	public static Image getPreviewImage(Blueprint bp) {

		if (bp == null || !bp.containsKey(BlueprintField.previewImage)) {

			return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		}
		return ((ImageIcon) bp.get(BlueprintField.previewImage)).getImage();
	}

	public static boolean hasLevel(int destination) {
		return levels.size() > destination && destination >= 0;
	}

	public static void setCurrentLevel(int destination) {
		if (hasLevel(destination)) {
			level = levels.get(destination);
			levelIndex = destination;
		} else {
			// App.print("Level with index " + destination
			// + " doesn't exist.");
			if (level != null) {
				// level.won = true;
			}
			return;
		}
	}

	public static void update() {
		if (level != null) {
			level.update();
		}
	}

	public static List<Entity> getEntities() {
		return level.getEntities();
	}

	public static void draw() {
		Graphic levelBG = level.getBackgroundGraphic();
		ViewManager.clear(levelBG.color);
		if (levelBG.hasTextureName()) {
			ViewManager.drawBG(levelBG.getTexture());
		}

		if (GameStateManager.at( State.Edit)) {
			ViewManager.drawEditorGrid(Editor.gridSize);
		}

		ViewManager.drawEntities(level.getEntities());

		if (GameStateManager.at(State.Edit)) {
			Screen currentScreen = GUIManager.getScreen();
			if (currentScreen instanceof Screen_Edit) {
				((Screen_Edit) currentScreen).draw();
			}
		}
	}

	public static void addEntity(Entity e) {
		level.addEntity(e);
	}

	public static int getLevelIndex() {
		return levelIndex;
	}

	public static Rectangle getLevelRect() {
		return level.getRect();
	}

	public static void addPremadeLevel(Level lvl) {
		levels.add(lvl);
	}

	public static Level getLevel() {
		return level;
	}

	public static void addBlankLevel() {
		levels.add(new Level(new LinkedList<Entity>()));
	}

	public static int getLevelCount() {
		return levels.size();
	}

	public static void removeLevel(int index) {
		if (hasLevel(index)) {
			levels.remove(index);
		} else {
			System.out.println("Can't delete level #" + index + " -- it doesn't exist");
		}
	}

	public static String getName() {
		return _name;
	}

	public static void replaceCurrentLevel(Level lvl) {
		level = lvl;
		levels.set(levelIndex, lvl);
	}

	public static void setPreviewImage(ImageIcon previewImage) {
		World.previewImage = previewImage;
	}

}
