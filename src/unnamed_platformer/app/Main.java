package unnamed_platformer.app;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.game.other.EntityCreator;
import unnamed_platformer.game.other.EntityInfoDB;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.res_mgt.ClassLookup;
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

	private static boolean hotkeysAllowed = true;

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long millisecDelta = TimeManager.tick();

			if (millisecDelta == 0) {
				continue;
			}

			InputManager.update();
			if (hotkeysAllowed) {
				handleHotkeys();
			}

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
			ViewManager.saveScreenshot();
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
		System.setProperty("net.java.games.input.librarypath",
				Ref.NATIVE_LIB_DIR);

		setupCloner();

		ClassLookup.init();
		Settings.init();
		TimeManager.init();
		EntityCreator.init();
		ResManager.init();
		SoundManager.preload();
		InputManager.init();
		GUIManager.init();
		EntityInfoDB.init();
		ViewManager.init();
		EntityInfoDB.loadTextureMappings();
		GUIManager.changeScreen(ScreenType.Title);
	}

	private static Cloner cloner = new Cloner();

	private static void setupCloner() {
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
		if (EntityInfoDB.isInitialized()) {
			EntityInfoDB.finish();
		}
		InputManager.finish();
		SoundManager.finish();
		ViewManager.finish();

	}

	public static void setHotKeysAllowed(boolean value) {
		hotkeysAllowed = value;
	}

}
