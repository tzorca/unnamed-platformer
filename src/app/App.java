package app;

import game.logic.EntityCreator;

import org.lwjgl.opengl.Display;

import app.gui.GUIManager;

public class App {

	public static State state = State.start;
	private static State nextState = null;

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

			ViewManager.update();

			GameManager.update(timeDelta);
		}
	}

	public static void main(String[] args) {
		init();
		gameLoop();
		end();
	}

	private static void init() {
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

	static boolean restartNextTick = false;

	public static void requestRestart() {
		restartNextTick = true;
	}

	public static void delayedStateChange(State n) {
		nextState = n;
	}

}
