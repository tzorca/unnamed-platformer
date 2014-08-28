package unnamed_platformer.app;

import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.Level;
import unnamed_platformer.game.World;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.lvl_gen.BaseLevelGenerator;
import unnamed_platformer.game.lvl_gen.ProceduralGenerator;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.GUIManager.ScreenType;

public class GameManager {

	private static boolean playing;
	
	public static boolean playing() {
		return playing;
	}
	
	public static void setPlaying(boolean value) {
		playing = value;
	}
	
	public static void draw() {
		World.draw();
	}

	public static void loadGame(String name) {
		World.load(name);
	}

	public static void playRandomGame() {
		World.reset("Randomly Generated Game", false);

		BaseLevelGenerator generator = new ProceduralGenerator();
		for (int i = 0; i < 10; i++) {
			World.addPremadeLevel(generator.generate());
		}

		World.setCurrentLevel(0);
		setPlaying(true);
		GUIManager.changeScreen(ScreenType.Play);
	}

	public static void addBlankLevel() {
		World.addBlankLevel();
	}

	public static void saveCurrentGame(String name) {
		World.save(name);
	}

	public static boolean saveCurrentGame() {
		return World.save(World.getName());
	}

	public static String getGameName() {
		return World.getName();
	}

	public static void changeLevelToIndex(int destIndex) {
		World.setCurrentLevel(destIndex);
	}

	public static List<Entity> getCurrentEntities() {
		return World.getEntities();
	}

	public static void addEntity(Entity e) {
		World.addEntity(e);
	}

	public static int getCurrentLevelNumber() {
		return World.getLevelIndex();
	}

	public static Rectangle getRect() {
		return World.getLevelRect();
	}

	public static Level getCurrentLevel() {
		return World.getLevel();
	}

	public static boolean levelExists(int index) {
		return World.hasLevel(index);
	}

	public static int getLevelCount() {
		return World.getLevelCount();
	}

	public static void removeLevelByIndex(int index) {
		World.removeLevel(index);
	}

	public static void replaceCurrentLevel(Level lvl) {
		World.replaceCurrentLevel(lvl);
	}

	public static void populateFromQuadTree(List<Entity> entities, Rectangle box) {
		getCurrentLevel().retrieveFromQuadTree(entities, box);

	}

}
