package unnamed_platformer.gui;

import java.util.LinkedList;

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

	private static LinkedList<ScreenType> screenStateStack = new LinkedList<ScreenType>();

	public static String stateAsString() {
		return top().toString();
	}

	public static void changeScreen(ScreenType newState) {
		if (newState == null) {
			System.out.println("Note: Exited by changing screen.");
			System.exit(0);
		}
		if (screen == null || screen.finish(newState)) {
			screenStateStack.add(newState);
			String className = "Screen_" + stateAsString();

			if (ClassLookup.classExists(GUIRef.PACKAGE_NAME, className)) {
				screen = (Screen) ClassLookup.instantiate(GUIRef.PACKAGE_NAME, className);
			} else {
				screen = (Screen) ClassLookup.instantiate(GUIRef.PACKAGE_NAME, "BaseScreen_Render");
			}
			ViewManager.setGUIPanel(screen.getPanel());
		}
	}

	public static ScreenType current() {
		return top();
	}

	public static boolean atScreen(ScreenType testState) {
		return top() == testState;
	}

	private static Screen screen;

	public static void init() {
		changeScreen(ScreenType.Start);
	}

	public static void update() {
		screen.update();
	}

	public static Screen getScreen() {
		return screen;
	}
	
	public static boolean canExit() {
		if (screen == null) {
			return true;
		}
		return screen.canExit();
	}

	public static void drawBackground() {
		screen.drawBackground();
	}

	public static void drawForeground() {
		screen.drawForeground();
	}

	private static ScreenType top() {
		return screenStateStack.get(screenStateStack.size() - 1);
	}

	public static void back() {
		// TODO Auto-generated method stub

	}
}
