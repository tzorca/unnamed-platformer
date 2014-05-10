package app;

import game.logic.EntityCreator;
import game.parameters.InputRef.GameKey;

import org.lwjgl.opengl.Display;

import app.gui.GUIManager;

public class App {

	public static State initialState = State.start;
	public static State state = initialState;
	public static State nextState = null;

	public static enum State {
		start, choosePlayLevel, chooseEditLevel, play, edit, editSelect, playSelect
	}

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long timeDelta = TimeManager.tick();

			if (timeDelta == 0) {
				continue;
			}

			if (nextState != null) {
				state = nextState;
				nextState = null;
			}

			InputManager.update();

			processSpecialInput();
			
			GameManager.update(timeDelta);

			ViewManager.update();
		}
	}

	private static void processSpecialInput() {
		if (InputManager.getGameKeyState(GameKey.restartApp, 1)) {
			GUIManager.setStateHeld(false);
			App.restart();
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

	public static void delayedStateChange(State n) {
		nextState = n;
	}

}
