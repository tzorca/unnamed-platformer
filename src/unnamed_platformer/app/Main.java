package unnamed_platformer.app;

import org.lwjgl.opengl.Display;

import unnamed_platformer.game.EntityCreator;
import unnamed_platformer.game.World;
import unnamed_platformer.globals.InputRef.GameKey;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.GUIManager.ScreenType;
import unnamed_platformer.res_mgt.ResManager;

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
			Main.restart();
		}

		else if (InputManager.gameKeyPressed(GameKey.menuBack, 1)) {
			GUIManager.back();
		}

		else if (InputManager.gameKeyPressed(GameKey.startRandomGame, 1)) {
			GameManager.playRandomGame();
		}

		else if (InputManager.gameKeyPressed(GameKey.saveTempGame, 1)) {
			GameManager.saveCurrentGame("Temp");
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
		SQLiteStuff.init();
		TimeManager.init();
		InputManager.init();
		EntityCreator.init();
		GUIManager.init();
	}

	public static void restart() {
		GUIManager.changeScreen(ScreenType.Start);
	}

}
