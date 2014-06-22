package unnamed_platformer.app;

import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.app.Main.State;
import unnamed_platformer.game.Game;
import unnamed_platformer.game.Level;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.lvl_gen.BaseLevelGenerator;
import unnamed_platformer.game.lvl_gen.ProceduralGenerator;

public class GameManager {

	public static Game currentGame;

	public static void update() {
		if (currentGame != null) {
			currentGame.update();
		}
	}

	public static void draw() {
		currentGame.draw();
	}

	public static void loadGame(String name) {
		currentGame = Game.load(name);
	}

	public static Game playRandomGame() {
		Game newGame = new Game("Randomly Generated Game", false);

		BaseLevelGenerator generator = new ProceduralGenerator();
		for (int i = 0; i < 10; i++) {
			newGame.addPremadeLevel(generator.generate());
		}

		newGame.setCurrentLevel(0);
		Main.state = State.Play;
		currentGame = newGame;
		return newGame;
	}

	public static void addBlankLevel() {
		currentGame.addBlankLevel();
	}

	public static void saveCurrentGame(String name) {
		currentGame.save(name);
	}

	public static void saveCurrentGame() {
		currentGame.save(currentGame.getName());
	}

	public static String getGameName() {
		return currentGame.getName();
	}

	public static void changeLevelToIndex(int destIndex) {
		currentGame.setCurrentLevel(destIndex);
	}

	public static List<Entity> getCurrentEntities() {
		return currentGame.getEntities();
	}

	public static void addEntity(Entity e) {
		currentGame.addEntity(e);
	}

	public static int getCurrentLevelNumber() {
		return currentGame.getLevelIndex();
	}

	public static Rectangle getRect() {
		return currentGame.getLevelRect();
	}

	public static Level getCurrentLevel() {
		return currentGame.getLevel();
	}

	public static boolean levelExists(int index) {
		return currentGame.hasLevel(index);
	}

	public static int getLevelCount() {
		return currentGame.getLevelCount();
	}

	public static void removeLevelByIndex(int index) {
		currentGame.removeLevel(index);
	}

	public static void replaceCurrentLevel(Level lvl) {
		currentGame.replaceCurrentLevel(lvl);
	}

	public static void retrieveFromQuadTree(List<Entity> entities, Rectangle box) {
		getCurrentLevel().retrieveFromQuadTree(entities, box);

	}

}
