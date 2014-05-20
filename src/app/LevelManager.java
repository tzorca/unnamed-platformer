package app;

import game.Level;
import game.entities.Entity;

import java.util.List;

import org.newdawn.slick.geom.Rectangle;

public class LevelManager {

	public static void changeLevel(int destination) {
		GameManager.currentGame.setCurrentLevel(destination);
	}

	public static List<Entity> getCurrentEntities() {
		return GameManager.currentGame.getEntities();
	}

	public static void addEntity(Entity e) {
		GameManager.currentGame.addEntity(e);
	}

	public static int getLevelNumber() {
		return GameManager.currentGame.getLevelIndex();
	}

	public static Rectangle getRect() {
		return GameManager.currentGame.getLevelRect();
	}

	public static Level getCurrentLevel() {
		return GameManager.currentGame.getLevel();
	}

	public static boolean levelExists(int index) {
		return GameManager.currentGame.hasLevel(index);
	}

	public static int getLevelCount() {
		return GameManager.currentGame.getLevelCount();
	}

	public static void removeLevel(int levelIndexToRemove) {
		GameManager.currentGame.removeLevel(levelIndexToRemove);
	}


	public static void replaceCurrentLevel(Level lvl) {
		GameManager.currentGame.replaceCurrentLevel(lvl);
	}

	public static void retrieveFromQuadTree(List<Entity> entities,
			Rectangle box) {
		getCurrentLevel().retrieveFromQuadTree(entities, box);
		
	}

}
