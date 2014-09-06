package unnamed_platformer.game.other;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.lvl_gen.BaseLevelGenerator;
import unnamed_platformer.game.lvl_gen.ProceduralGenerator;
import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

public final class World
{
	private static List<Level> levels = new LinkedList<Level>();
	private static Level level; // current level
	private static int levelIndex = 0;
	private static String localName;
	private static ImageIcon previewImage = null;
	private static boolean playing;

	public static boolean playing() {
		return playing;
	}

	public static void setPlaying(boolean value) {
		playing = value;
	}

	public static void playRandomGame() {
		reset("Randomly Generated Game", false);

		BaseLevelGenerator generator = new ProceduralGenerator();
		for (int i = 0; i < 10; i++) {
			World.addPremadeLevel(generator.generate());
		}

		setLevelByIndex(0);
		setPlaying(true);
		GUIManager.changeScreen(ScreenType.Play);
	}

	public static void populateFromQuadTree(List<Entity> entities, Rectangle box) {
		getCurrentLevel().retrieveFromQuadTree(entities, box);
	}

	public static boolean saveCurrentGame() {
		return save(getName());
	}

	public static void reset(final String name, final boolean addBlank) {
		localName = name;
		levels.clear();
		if (addBlank) {
			addBlankLevel();
		}
		setLevelByIndex(0);
	}

	public static boolean save(String name) {
		localName = name;
		String filename = ResManager.getFilename(World.class, name);
		return toBlueprint().save(filename);
	}

	public static void load(String name) {
		reset(name, false);
		String filename = ResManager.getFilename(World.class, name);
		fromBlueprint(Blueprint.load(filename, false), name);
	}

	public static Blueprint toBlueprint() {
		Blueprint worldBlueprint = new Blueprint();

		worldBlueprint.put(BlueprintField.PREVIEW_IMAGE, previewImage);

		List<Blueprint> levelBlueprints = new LinkedList<Blueprint>();
		for (Level lvl : levels) {
			levelBlueprints.add(lvl.toBlueprint());
		}

		worldBlueprint.put(BlueprintField.LEVEL_DATA, levelBlueprints);

		return worldBlueprint;
	}

	@SuppressWarnings("unchecked")
	private static void fromBlueprint(Blueprint bp, String name) {
		if (bp == null) {
			System.out
					.println("Currently existing blueprint is invalid. Creating new game...");
			reset(name, true);
			return;
		}

		List<Blueprint> lBPs = (LinkedList<Blueprint>) bp
				.get(BlueprintField.LEVEL_DATA);

		reset(name, false);

		for (Blueprint lvlBlueprint : lBPs) {
			levels.add(Level.fromBlueprint(lvlBlueprint));
		}

		setLevelByIndex(0);
	}

	public static Image getPreviewImage(Blueprint bp) {

		if (bp == null || !bp.containsKey(BlueprintField.PREVIEW_IMAGE)) {

			return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		}
		return ((ImageIcon) bp.get(BlueprintField.PREVIEW_IMAGE)).getImage();
	}

	public static boolean hasLevelIndex(int destination) {
		return levels.size() > destination && destination >= 0;
	}

	public static void setLevelByIndex(int destination) {
		if (hasLevelIndex(destination)) {
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
		levels.add(lvl);
	}

	public static Level getCurrentLevel() {
		return level;
	}

	public static void addBlankLevel() {
		levels.add(new Level(new LinkedList<Entity>()));
	}

	public static int getLevelCount() {
		return levels.size();
	}

	public static void removeLevelByIndex(int index) {
		if (hasLevelIndex(index)) {
			levels.remove(index);
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
		levels.set(levelIndex, lvl);
	}

	public static void setPreviewImage(ImageIcon previewImage) {
		World.previewImage = previewImage;
	}

	public static int getCurrentLevelIndex() {
		return levelIndex;
	}

}
