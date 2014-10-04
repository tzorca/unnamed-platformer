package unnamed_platformer.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.UIManager;

import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.dialogs.Dialog;
import unnamed_platformer.view.gui.screens.Screen;

// TODO: Add button/key to return to previous menu
// TODO: Implement pause key functionality (return to ...)
// TODO: Add options screen
public final class GUIManager
{
	public static enum ScreenType {
		Edit, Play, SelectWorld, Start, Transition
	}

	public static final Color COLOR_WHITE = Color.white;
	public static final Color COLOR_LIGHT_GREY = new Color(0xee, 0xee, 0xee);
	public static final Color COLOR_DARK_BLUE_1 = new Color(0x10, 0x10, 0x20);
	public static final Color COLOR_DARK_BLUE_2 = new Color(0x20, 0x20, 0x30);
	public static final Color COLOR_DARK_BLUE_3 = new Color(0x30, 0x30, 0x40);
	public static final Color COLOR_DARK_BLUE_4 = new Color(0x40, 0x40, 0x50);
	public static final Color COLOR_HIGHLIGHT_BLUE = new Color(0x30, 0x30, 0x60);

	public static final String CENTER_LAYOUT = "pushx, alignx center, wrap";

	public static final Font HEADING_FONT = new Font("Calibri", Font.PLAIN, 36);
	public static final Font SUB_HEADING_FONT = new Font("Tahoma", Font.PLAIN,
			18);

	public static final String SCREEN_PACKAGE_NAME = Ref.BASE_PACKAGE_NAME
			+ ".view.gui.screens";

	private static Screen screen;
	private static Dialog dialog;

	private static LinkedList<ScreenType> screenStateStack = new LinkedList<ScreenType>();

	static {
		// enable native look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			System.exit(0);
		}
		if (screen == null || screen.finish(newState)) {
			screenStateStack.add(newState);
			String className = "Screen_" + stateAsString();

			if (ClassLookup.classExists(GUIManager.SCREEN_PACKAGE_NAME,
					className)) {
				screen = (Screen) ClassLookup.instantiate(
						GUIManager.SCREEN_PACKAGE_NAME, className);
			} else {
				screen = (Screen) ClassLookup.instantiate(
						GUIManager.SCREEN_PACKAGE_NAME, "BaseScreen_Render");
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
