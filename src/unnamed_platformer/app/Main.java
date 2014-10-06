package unnamed_platformer.app;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.InputManager.GameKey;
import unnamed_platformer.game.other.EntityCreator;
import unnamed_platformer.game.other.QuadTree;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

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
		if (InputManager.keyPressOccurred(GameKey.RESTART_APP, 1)) {
			GUIManager.changeScreen(ScreenType.SelectWorld);
		}

		// else if (InputManager.keyPressOccurred(GameKey.back, 1)) {
		// GUIManager.back();
		// }
		// else if (InputManager.gameKeyPressed(GameKey.saveTempGame, 1)) {
		// World.save("Temp"); }

		else if (InputManager.keyPressOccurred(GameKey.SAVE_SCREENSHOT, 1)) {
			ImageHelper.saveScreenshot();
		}

		else if (InputManager.keyPressOccurred(GameKey.TOGGLE_FULLSCREEN, 1)) {
			ViewManager.toggleFullscreen();
		}

	}

	public static void main(String[] args) {
		init();
		gameLoop();
	}

	private static void init() {
		// used to load lwjgl libraries without relying on project configuration
		System.setProperty("org.lwjgl.librarypath", Ref.LWJGL_NATIVE_DIR);

		TimeManager.init();
		ViewManager.init();
		ResManager.init();
		GUIManager.changeScreen(ScreenType.SelectWorld);
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

}
