package unnamed_platformer.game.zones;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.app.Main;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.FileGlobals;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

public final class World implements Serializable
{
	private static final long serialVersionUID = 2605757003325366045L;

	public transient static World current = new World();

	private List<Level> levels = new LinkedList<Level>();
	private static transient Level level; // current level
	private static transient int levelIndex = 0;
	private static transient String localName;
	private static transient boolean playing;

	public static boolean playing() {
		return playing;
	}

	public static void setPlaying(boolean value) {
		playing = value;
	}

	public static void reset(final boolean addBlank) {
		current.levels.clear();
		if (addBlank) {
			addBlankLevel();
		}
		setLevelByIndex(0);
	}

	public static boolean save() {
		return save(getName());
	}

	public static boolean save(String name) {
		localName = name;

		String data = Main.getGson().toJson(current);
		String filename = FileGlobals.GAME_DIR + name + FileGlobals.GAME_EXT;
		File file = new File(filename);
		try {
			FileUtils.writeStringToFile(file, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final int MIN_WORLD_CHAR_LENGTH = 10;

	public static void load(String name) {
		localName = name;
		reset(false);

		String filename = FileGlobals.GAME_DIR + name + FileGlobals.GAME_EXT;
		File file = new File(filename);
		String data = null;
		try {
			data = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (data != null && data.length() >= MIN_WORLD_CHAR_LENGTH) {
			try {
				current = Main.getGson().fromJson(data, World.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			World.addBlankLevel();
		}

		setLevelByIndex(0);

	}

	public static boolean hasLevelIndex(int destination) {
		return current.levels.size() > destination && destination >= 0;
	}

	public static void setLevelByIndex(int destination) {
		levelIndex = destination;
		if (hasLevelIndex(destination)) {
			level = current.levels.get(destination);
		} else {
			// App.print("Level with index " + destination
			// + " doesn't exist.");
			return;
		}
	}

	public static void update() {
		if (level != null) {
			level.update();
		}
	}

	public List<Entity> getEntities() {
		return level.getEntities();
	}

	public static void drawBackground() {
		if (GUIManager.atScreen(ScreenType.Play)
				|| GUIManager.atScreen(ScreenType.Edit)) {
			Display.setTitle(World.getName());

			level.drawBackground();
		}
	}

	public static void drawForeground() {
		if (GUIManager.atScreen(ScreenType.Play)
				|| GUIManager.atScreen(ScreenType.Edit)) {
			Display.setTitle(World.getName());

			level.drawForeground();
		}
	}

	public static void addEntity(final Entity entity) {
		level.addEntity(entity);
	}

	public static Rectangle getLevelRect() {
		return level.getRect();
	}

	public static void addPremadeLevel(final Level lvl) {
		current.levels.add(lvl);
	}

	public static Level getCurrentLevel() {
		return level;
	}

	public static void addBlankLevel() {
		current.levels.add(new Level(new LinkedList<Entity>()));
	}

	public static int getLevelCount() {
		return current.levels.size();
	}

	public static void removeLevelByIndex(int index) {
		if (hasLevelIndex(index)) {
			current.levels.remove(index);
		} else {
			System.out.println("Can't delete level #" + index
					+ " -- it doesn't exist");
		}
	}

	public static String getName() {
		return localName;
	}

	public static void replaceCurrentLevel(final Level lvl) {
		level = lvl;
		current.levels.set(levelIndex, lvl);
	}

	public static int getCurrentLevelIndex() {
		return levelIndex;
	}

}
