package app;

import game.logic.EntityCreator;
import game.parameters.InputRef.GameKey;
import game.parameters.Ref;
import game.parameters.ViewRef;

import org.lwjgl.opengl.Display;

import app.gui.GUIManager;

public class App {

	public static State initialState = State.start;
	public static State state = initialState;

	public static enum State {
		start, choosePlayLevel, chooseEditLevel, play, edit, editSelect, playSelect
	}

	static long accumulator = 0;

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long millisecDelta = TimeManager.tick();

			sync();

			if (millisecDelta == 0) {
				continue;
			}

			InputManager.update();
			processSpecialInput();

			accumulator += millisecDelta;

			while (accumulator >= Ref.MILLISECS_IN_IDEAL_TIC) {
				GameManager.update(Ref.MILLISECS_IN_IDEAL_TIC);
				accumulator -= Ref.MILLISECS_IN_IDEAL_TIC;
			}

			ViewManager.update();

		}
	}

	private static void sync() {
		// According to LWJGL documentation, this should be 
		// called in the game loop
		Display.sync(ViewRef.FPS);
	}

	private static void processSpecialInput() {
		if (InputManager.getGameKeyState(GameKey.restartApp, 1)) {
			GUIManager.setStateHeld(false);
			App.restart();
		}

		if (InputManager.getGameKeyState(GameKey.startRandomGame, 1)) {
			GameManager.generateRandomGame();
			App.state = State.play;
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
		state = State.start;

		// aMgr.loadSample("beep", "beep.wav");
		// aMgr.play("beep");
	}

	public static void log(String string) {
		System.out.println(string);
	}

}
