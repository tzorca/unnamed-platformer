package unnamed_platformer.gui;

import unnamed_platformer.app.App;
import unnamed_platformer.app.App.State;
import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.parameters.GUIRef;

// TODO: Add button/key to return to previous menu
// TODO: Implement pause key functionality (return to ...)
// TODO: Add options screen
public class GUIManager {

	private static Screen screen;

	public static void init() {
		screenChange();
	}

	private static State lastState = State.Start;
	private static boolean stateHeld = false;

	public static boolean isStateHeld() {
		return stateHeld;
	}

	public static void setStateHeld(boolean holdState) {
		GUIManager.stateHeld = holdState;
	}

	public static void update() {
		if (App.state != lastState && !stateHeld) {
			screenChange();
			lastState = App.state;
		}

		screen.update();
	}

	private static void screenChange() {
		String className = "Screen_" + App.state.toString();

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

}
