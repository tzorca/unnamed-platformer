package unnamed_platformer.app;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.InputManager.GameKey;
import unnamed_platformer.game.other.EntityCreator;
import unnamed_platformer.game.other.QuadTree;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.res_mgt.SoundManager;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.almworks.sqlite4java.SQLite;
import com.rits.cloning.Cloner;

public final class Main
{

	private static long accumulator = 0;

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
		if (InputManager.keyPressOccurred(GameKey.RESTART, 1)) {
			GUIManager.changeScreen(ScreenType.Title);
		}

		else if (InputManager.keyPressOccurred(GameKey.SAVE_SCREENSHOT, 1)) {
			ImageHelper.saveScreenshot();
		}

		else if (InputManager.keyPressOccurred(GameKey.TOGGLE_FULLSCREEN, 1)) {
			ViewManager.toggleFullscreen();
		}

		else if (InputManager.keyPressOccurred(GameKey.EXIT, 1)) {
			doHalt();
		}

		// else if (InputManager.keyPressOccurred(GameKey.back, 1)) {
		// GUIManager.back();
		// }
		// else if (InputManager.gameKeyPressed(GameKey.saveTempGame, 1)) {
		// World.save("Temp");
		// }

	}

	public static void main(String[] args) {
		init();
		gameLoop();
	}

	private static void init() {
		// used to load native libraries without relying on project
		// configuration
		SQLite.setLibraryPath(Ref.NATIVE_LIB_DIR);
		System.setProperty("org.lwjgl.librarypath", Ref.NATIVE_LIB_DIR);

		Settings.init();
		TimeManager.init();
		ViewManager.init();
		ResManager.init();
		GUIManager.changeScreen(ScreenType.Title);
		SQLiteStuff.init();
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

	public static void doExit() {
		deinit();
		System.exit(0);
	}

	public static void doHalt() {
		deinit();
		Runtime.getRuntime().halt(0);
	}

	public static void deinit() {
		if (SQLiteStuff.isInitialized()) {
			SQLiteStuff.finish();
		}
		SoundManager.finish();

	}

}
