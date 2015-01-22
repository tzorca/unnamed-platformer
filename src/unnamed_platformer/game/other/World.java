package unnamed_platformer.game.other;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.app.Main;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.lvl_gen.BaseLevelGenerator;
import unnamed_platformer.game.lvl_gen.ProceduralGenerator;
import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.google.gson.Gson;

public final class World implements Serializable
{
	private static final long serialVersionUID = 2605757003325366045L;

	public transient static World current = new World();

	private List<Level> levels = new LinkedList<Level>();
	private transient Level level; // current level
	private transient int levelIndex = 0;
	private transient String localName;
	private transient ImageIcon previewImage = null;
	private transient boolean playing;

	public static boolean playing() {
		return current.playing;
	}

	public static void setPlaying(boolean value) {
		current.playing = value;
	}

	public static void playRandomGame() {
		reset("Randomly Generated Game", false);

		BaseLevelGenerator generator = new ProceduralGenerator();
		for (int i = 0; i < 10; i++) {
			addPremadeLevel(generator.generate());
		}

		setLevelByIndex(0);
		setPlaying(true);
		GUIManager.changeScreen(ScreenType.Play);
	}

	public static void reset(final String name, final boolean addBlank) {
		current.localName = name;
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
		current.localName = name;
		String filename = ResManager.getFilename(World.class, name);
		return toBlueprint().save(filename);
	}

	public static boolean saveGson(String name) {
		current.localName = name;
		String data = null;

		try {
			data = Main.getGson().toJson(current);
			System.out.println(data);
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
//		String filename = ResManager.getFilename(World.class, name);
//		File file = new File(filename);
//		try {
//			FileUtils.writeStringToFile(file, data);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}

	public static void load(String name) {
		reset(name, false);
		String filename = ResManager.getFilename(World.class, name);

		Blueprint bp = Blueprint.load(filename, false);
		fromBlueprint(bp, name);
	}

	public static Blueprint toBlueprint() {
		Blueprint worldBlueprint = new Blueprint();

		worldBlueprint.put(BlueprintField.PREVIEW_IMAGE, current.previewImage);

		List<Blueprint> levelBlueprints = new LinkedList<Blueprint>();
		for (Level lvl : current.levels) {
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
			current.levels.add(Level.fromBlueprint(lvlBlueprint));
		}

		setLevelByIndex(0);
	}

	public Image getPreviewImage(Blueprint bp) {

		if (bp == null || !bp.containsKey(BlueprintField.PREVIEW_IMAGE)) {

			return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		}
		return ((ImageIcon) bp.get(BlueprintField.PREVIEW_IMAGE)).getImage();
	}

	public static boolean hasLevelIndex(int destination) {
		return current.levels.size() > destination && destination >= 0;
	}

	public static void setLevelByIndex(int destination) {
		current.levelIndex = destination;
		if (hasLevelIndex(destination)) {
			current.level = current.levels.get(destination);
		} else {
			// App.print("Level with index " + destination
			// + " doesn't exist.");
			return;
		}
	}

	public static void update() {
		if (current.level != null) {
			current.level.update();
		}
	}

	public List<Entity> getEntities() {
		return level.getEntities();
	}

	public static void drawBackground() {
		if (GUIManager.atScreen(ScreenType.Play)
				|| GUIManager.atScreen(ScreenType.Edit)) {
			Display.setTitle(World.getName());

			current.level.drawBackground();
		}
	}

	public static void drawForeground() {
		if (GUIManager.atScreen(ScreenType.Play)
				|| GUIManager.atScreen(ScreenType.Edit)) {
			Display.setTitle(World.getName());

			current.level.drawForeground();
		}
	}

	public static void addEntity(final Entity entity) {
		current.level.addEntity(entity);
	}

	public static Rectangle getLevelRect() {
		return current.level.getRect();
	}

	public static void addPremadeLevel(final Level lvl) {
		current.levels.add(lvl);
	}

	public static Level getCurrentLevel() {
		return current.level;
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
		return current.localName;
	}

	public static void replaceCurrentLevel(final Level lvl) {
		current.level = lvl;
		current.levels.set(current.levelIndex, lvl);
	}

	public void setPreviewImage(ImageIcon previewImage) {
		this.previewImage = previewImage;
	}

	public static int getCurrentLevelIndex() {
		return current.levelIndex;
	}

}
