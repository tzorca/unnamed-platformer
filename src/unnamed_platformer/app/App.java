package unnamed_platformer.app;

import org.lwjgl.opengl.Display;

import unnamed_platformer.game.logic.EntityCreator;
import unnamed_platformer.game.parameters.Ref;
import unnamed_platformer.game.parameters.InputRef.GameKey;
import unnamed_platformer.gui.GUIManager;

public class App {

	public static State initialState = State.Start;
	public static State state = initialState;

	public static enum State {
		Start, ChoosePlayLevel, ChooseEditLevel, Play, Edit, EditSelect, PlaySelect
	}

	static long accumulator = 0;

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long millisecDelta = TimeManager.tick();

			if (millisecDelta == 0) {
				continue;
			}

			InputManager.update();
			processSpecialInput();

			accumulator += millisecDelta;

			while (accumulator >= Ref.MILLISECS_IN_IDEAL_TIC) {
				GameManager.update();
				accumulator -= Ref.MILLISECS_IN_IDEAL_TIC;
			}

			ViewManager.update();

		}
	}

	private static void processSpecialInput() {
		if (InputManager.getGameKeyState(GameKey.restartApp, 1)) {
			GUIManager.setStateHeld(false);
			App.restart();
		}

		if (InputManager.getGameKeyState(GameKey.startRandomGame, 1)) {
			GameManager.generateRandomGame();
			App.state = State.Play;
		}

		if (InputManager.getGameKeyState(GameKey.saveTempGame, 1)) {
			GameManager.saveCurrentGame("Temp");
		}
	}

	public static void main(String[] args) {
		init();
		gameLoop();
		end();
	}

	private static void init() {
		LibraryLoader.init();
		ViewManager.init();
		TimeManager.init();
		InputManager.init();
		ContentManager.init();
		EntityCreator.init();
		GUIManager.init();
		GameManager.init();
	}

	private static void end() {
		Display.destroy();
	}

	public static void restart() {
		state = State.Start;

		// aMgr.loadSample("beep", "beep.wav");
		// aMgr.play("beep");
	}

	public static void log(String string) {
		App.print(string);
	}

	public static void print(Object o) {
		App.print(o + "");
	}

}
