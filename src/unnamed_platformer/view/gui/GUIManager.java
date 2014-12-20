package unnamed_platformer.view.gui;

import java.awt.Font;
import java.util.LinkedList;

import javax.swing.UIManager;

import unnamed_platformer.app.Main;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.view.FluidColor;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.dialogs.Dialog;
import unnamed_platformer.view.gui.screens.Screen;

// TODO: Implement pause key functionality (return to ...)
public final class GUIManager
{
	public static enum ScreenType {
		Title, Edit, Play, SelectWorld, Transition, Options
	}

	public static final FluidColor
	/* */COLOR_LIGHT_GREY = new FluidColor(0xee, 0xee, 0xee),
	/* */COLOR_ORANGE = new FluidColor(0xbb, 0x60, 0x40),
	/* */COLOR_MAIN = new FluidColor(0x20, 0x20, 0x40).incrementHue(-0.1f),
	/* */COLOR_MAIN_MINUS, COLOR_MAIN_PLUS, COLOR_MAIN_PLUS_PLUS,
			COLOR_MAIN_HIGHLIGHT;

	// Dynamically generate other colors from main color
	static {

		COLOR_MAIN_MINUS = COLOR_MAIN.darker();

		COLOR_MAIN_PLUS = COLOR_MAIN.brighter();

		COLOR_MAIN_PLUS_PLUS = COLOR_MAIN.brighter(2);
		COLOR_MAIN_HIGHLIGHT = COLOR_MAIN_PLUS.highlight();
	}

	public static final String CENTER_LAYOUT = "pushx, alignx center, wrap";

	private static final int BASE_FONT_SIZE = 20,
			LARGER_FONT_SIZE = (int) (BASE_FONT_SIZE * 1.5),
			LARGEST_FONT_SIZE = (int) (BASE_FONT_SIZE * 2.0),
			SMALLER_FONT_SIZE = (int)(BASE_FONT_SIZE * 0.7);

	private static final String FONT_NAME = "Trebuchet MS";

	public static final Font
	/* */FONT_HEADING = new Font(FONT_NAME, Font.PLAIN, LARGEST_FONT_SIZE),
	/* */FONT_SUB_HEADING = new Font(FONT_NAME, Font.PLAIN, LARGER_FONT_SIZE),
	/* */FONT_NORMAL = new Font(FONT_NAME, Font.PLAIN, BASE_FONT_SIZE),
	/* */FONT_SMALL = new Font(FONT_NAME, Font.PLAIN, SMALLER_FONT_SIZE),
	/* */FONT_HUD = new Font("Lucida Console", Font.PLAIN, BASE_FONT_SIZE);

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
			Main.doExit();
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
