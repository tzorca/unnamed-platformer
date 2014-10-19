package unnamed_platformer.view.gui.screens;

import java.awt.Panel;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

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

		InputManager.resetEventHandlers();
		pnlSurface.setBackground(GUIManager.COLOR_DARK_BLUE_3);
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
