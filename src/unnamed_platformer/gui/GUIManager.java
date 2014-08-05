package unnamed_platformer.gui;

import unnamed_platformer.app.GameStateManager;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.globals.GUIRef;
import unnamed_platformer.res_mgt.ClassLookup;

// TODO: Add button/key to return to previous menu
// TODO: Implement pause key functionality (return to ...)
// TODO: Add options screen
public class GUIManager {

	private static Screen screen;

	public static void init() {
		screenChange();
	}

	public static void update() {
		if (GameStateManager.wasChanged()) {
			screenChange();
		}

		screen.update();
	}

	private static void screenChange() {
		String className = "Screen_" + GameStateManager.stateAsString();

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
