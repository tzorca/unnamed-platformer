package unnamed_platformer.view.screens;

import java.awt.Panel;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.app.Main;
import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.view.GUIManager.ScreenType;

public abstract class Screen
{
	protected Panel pnlSurface = new Panel(new MigLayout());

	public void update() {
	}

	public Panel getPanel() {
		return pnlSurface;
	}

	// instance initializer
	public Screen() {
		super();
		InputManager.resetEvents();
		Main.setHotKeysAllowed(true);
		pnlSurface.setBackground(StyleGlobals.COLOR_MAIN_PLUS);
	}

	final public boolean canExit() {
		return onFinish(null);
	}

	final public boolean finish(ScreenType plannedNextScreen) {
		if (!onFinish(plannedNextScreen)) {
			return false;
		}
		pnlSurface.removeAll();
		pnlSurface.setEnabled(false);
		pnlSurface.setVisible(false);
		return true;
	}

	/**
	 * @return true if finishing is allowed
	 */
	public boolean onFinish(ScreenType plannedNextScreen) {
		return true;
	}

	public void drawBackground() {
	}

	public void drawForeground() {
	}
}
