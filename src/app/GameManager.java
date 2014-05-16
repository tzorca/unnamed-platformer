package app;

import game.Game;
import game.logic.levelGenerators.LevelGenerator;
import game.logic.levelGenerators.SimpleProceduralGenerator;

public class GameManager {

	public static Game currentGame = new Game("Blank", true);

	public static void init() {
		// currentGame = GameManager.generateRandomGame();
		// saveGame();
		// loadGame("test");
	}

	public static void update() {
		currentGame.update();
	}

	public static void draw() {
		currentGame.draw();
	}

	public static void loadGame(String name) {
		currentGame = Game.load(name);
	}

	public static Game generateRandomGame() {
		Game newGame = new Game("Randomly Generated Game", false);
		
		LevelGenerator generator = new SimpleProceduralGenerator();
		for (int i = 0; i < 10; i++) {
			newGame.addPremadeLevel(generator.generate());
		}
		
		newGame.setCurrentLevel(0);
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

}
