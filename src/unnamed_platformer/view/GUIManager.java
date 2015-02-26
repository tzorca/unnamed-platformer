package unnamed_platformer.view;

import java.util.LinkedList;

import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.Main;
import unnamed_platformer.globals.ViewGlobals;
import unnamed_platformer.view.dialogs.Dialog;
import unnamed_platformer.view.screens.Screen;

// TODO: Implement pause key functionality (return to ...)
public final class GUIManager
{
	public static enum ScreenType {
		Title, Edit, Play, SelectWorld, Transition, Options
	}

	private static Screen screen;
	private static Dialog dialog;

	private static LinkedList<ScreenType> screenStateStack = new LinkedList<ScreenType>();

	public static void init() {
		// enable anti-aliased text:
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
	}

	public static boolean atScreen(ScreenType testState) {
		return top() == testState;
	}

	public static void back() {
		screenStateStack.removeLast();
		changeScreen(top());
	}

	public static boolean canExit() {
		if (screen == null) {
			return true;
		}
		return screen.canExit();
	}

	public static void changeScreen(ScreenType newState) {
		if (newState == null) {
			System.out.println("Note: Exited by changing screen.");
			Main.doExit();
		}
		if (screen == null || screen.finish(newState)) {
			screenStateStack.add(newState);
			String className = "Screen_" + stateAsString();

			if (ClassLookup.classExists(ViewGlobals.SCREEN_PACKAGE_NAME,
					className)) {
				screen = (Screen) ClassLookup.instantiate(
						ViewGlobals.SCREEN_PACKAGE_NAME, className);
			} else {
				screen = (Screen) ClassLookup.instantiate(
						ViewGlobals.SCREEN_PACKAGE_NAME, "BaseScreen_Render");
			}
			ViewManager.changeGUIPanel(screen.getPanel());
		}
	}

	public static ScreenType current() {
		return top();
	}

	public static void drawBackground() {
		screen.drawBackground();
	}

	public static void drawForeground() {
		screen.drawForeground();
	}

	public static Screen getScreen() {
		return screen;
	}

	public static String stateAsString() {
		return top().toString();
	}

	private static ScreenType top() {
		return screenStateStack.getLast();
	}

	public static void update() {
		// if a dialog exists and is visible
		// update it instead of the screen
		if (dialog != null && dialog.isVisible()) {
			dialog.update();
		} else {
			screen.update();
		}
	}

	public static ScreenType getPreviousScreen() {
		if (screenStateStack.size() > 1) {
			return screenStateStack.get(screenStateStack.size() - 2);
		} else {
			// No previous screen exists
			return top();
		}
	}

	public static void showDialog(Dialog newDialog) {
		dialog = newDialog;
		dialog.setVisible(true);
	}
}
