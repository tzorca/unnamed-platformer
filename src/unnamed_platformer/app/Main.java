package unnamed_platformer.app;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.rits.cloning.Cloner;

import unnamed_platformer.game.EntityCreator;
import unnamed_platformer.game.World;
import unnamed_platformer.globals.InputRef.GameKey;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.GUIManager.ScreenType;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.QuadTree;

public class Main {

	static long accumulator = 0;

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long millisecDelta = TimeManager.tick();

			if (millisecDelta == 0) {
				continue;
			}

			InputManager.update();
			handleHotkeys();

			accumulator += millisecDelta;

			while (accumulator >= Ref.MILLISECS_IN_IDEAL_TIC) {
				World.update();
				accumulator -= Ref.MILLISECS_IN_IDEAL_TIC;
			}

			ViewManager.update();

		}
	}

	private static void handleHotkeys() {
		if (InputManager.gameKeyPressed(GameKey.restartApp, 1)) {
			GUIManager.changeScreen(ScreenType.SelectWorld);
		}

		else if (InputManager.gameKeyPressed(GameKey.menuBack, 1)) {
			GUIManager.back();
		}

		else if (InputManager.gameKeyPressed(GameKey.startRandomGame, 1)) {
			World.playRandomGame();
		}

		else if (InputManager.gameKeyPressed(GameKey.saveTempGame, 1)) {
			World.save("Temp");
		}

		else if (InputManager.gameKeyPressed(GameKey.saveScreenshot, 1)) {
			ImageHelper.saveScreenshot();
		}

	}

	public static void main(String[] args) {
		init();
		gameLoop();
	}

	private static void init() {
		// used to load lwjgl libraries without relying on project configuration
		System.setProperty("org.lwjgl.librarypath", Ref.LWJGL_NATIVE_DIR);

		ViewManager.init();
		ResManager.init();
		GUIManager.changeScreen(ScreenType.SelectWorld);
		SQLiteStuff.init();
		TimeManager.init();
		InputManager.init();
		EntityCreator.init();
	}
	
	private static Cloner cloner = new Cloner();
	static {
		cloner.dontClone(QuadTree.class);
		cloner.dontClone(Texture.class);
	}

	public static <T> T deepClone(T o) {
		return cloner.deepClone(o);
	}

}
