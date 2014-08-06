package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;
import unnamed_platformer.globals.GUIRef;
import unnamed_platformer.res_mgt.ClassLookup;

// TODO: Add button/key to return to previous menu
// TODO: Implement pause key functionality (return to ...)
// TODO: Add options screen
public class GUIManager {

	public static enum ScreenType {
		Start, SelectWorld, Play, Edit
	}

	public final static ScreenType initialState = ScreenType.Start;
	private static ScreenType lastState = ScreenType.Start;
	public static ScreenType state = initialState;

	// Note: this event is only captured once
	public static boolean menuWasChanged() {
		if (state == lastState) {
			return false;
		}

		lastState = state;
		return true;
	}

	public static String stateAsString() {
		return state.toString();
	}

	public static void changeScreen(ScreenType newState) {
		state = newState;
	}

	public static ScreenType current() {
		return state;
	}

	public static boolean atScreen(ScreenType testState) {
		return state == testState;
	}

	private static Screen screen;

	public static void init() {
		screenChange();
	}

	public static void update() {
		if (menuWasChanged()) {
			screenChange();
		}

		screen.update();
	}

	private static void screenChange() {
		String className = "Screen_" + state.toString();

		if (screen != null) {
			screen.finish();
		}

		if (ClassLookup.classExists(GUIRef.PACKAGE_NAME, className)) {
			screen = (Screen) ClassLookup.instantiate(GUIRef.PACKAGE_NAME,
					className);
		} else {
			screen = (Screen) ClassLookup.instantiate(GUIRef.PACKAGE_NAME,
					"BaseScreen_Render");
		}
		ViewManager.setGUIPanel(screen.getPanel());
	}

	public static Screen getScreen() {
		return screen;
	}

	public static void drawBackground() {
		screen.drawBackground();
	}
	public static void drawForeground() {
		screen.drawForeground();
	}
}
