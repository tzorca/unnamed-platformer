package app;

import model.Game;
import model.Level;
import model.logic.LevelGenerator;

public class GameManager {

	public static Game currentGame = new Game("Blank", true);

	public static void init() {
		// currentGame = GameManager.generateRandomGame();
		// saveGame();
		// loadGame("test");
	}

	public static void update(long timeDelta) {
		currentGame.update(timeDelta);
	}

	public static void draw() {
		currentGame.draw();
	}

	public static void loadGame(String name) {
		currentGame = Game.load(name);
	}

	public static Game generateRandomGame() {
		Game newGame = new Game("Randomly Generated Game", false);
		for (int i = 0; i < 10; i++) {

			Level lvl = LevelGenerator.generateLevel();
			newGame.addPremadeLevel(lvl);
		}
		newGame.setCurrentLevel(0);
		currentGame = newGame;
		return newGame;
	}

	public static void addBlankLevel() {
		currentGame.addBlankLevel();
	}

	public static void saveCurrentGame() {
		currentGame.save(currentGame.getName());
	}

	public static String getGameName() {
		return currentGame.getName();
	}

}
