package unnamed_platformer.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.UIManager;

import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.screens.Screen;

// TODO: Add button/key to return to previous menu
// TODO: Implement pause key functionality (return to ...)
// TODO: Add options screen
public final class GUIManager
{

	public static enum ScreenType {
		Edit, Play, SelectWorld, Start, Transition
	}

	public static final Color GUI_BG_COLOR = new Color(0x16, 0x17, 0x26);
	public static final Color GUI_FG_COLOR = Color.white;
	public static final Color GUI_BG_ALT_COLOR= new Color(0x10, 0x10, 0x20);
	public static final Color GUI_BORDER_COLOR = new Color(0x26, 0x27, 0x38);

	public static final String CENTER_LAYOUT = "pushx, alignx center, wrap";

	public static final Font HEADING_FONT = new Font("Tahoma", Font.PLAIN, 48);

	public static final String SCREEN_PACKAGE_NAME = Ref.BASE_PACKAGE_NAME
			+ ".view.gui.screens";

	private static Screen screen;

	private static LinkedList<ScreenType> screenStateStack = new LinkedList<ScreenType>();

	public static final Font SUB_HEADING_FONT = new Font("Tahoma", Font.PLAIN,
			18);

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
			ViewManager.setGUIPanel(screen.getPanel());
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
		screen.update();
	}

	public static ScreenType getPreviousScreen() {
		if (screenStateStack.size() > 1) {
			return screenStateStack.get(screenStateStack.size() - 2);
		} else {
			// No previous screen exists
			return top();
		}
	}
}
