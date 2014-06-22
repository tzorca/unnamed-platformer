package unnamed_platformer.gui;

import java.awt.Panel;

import unnamed_platformer.app.InputManager;
import net.miginfocom.swing.MigLayout;

public abstract class Screen {
	protected Panel pnlSurface = new Panel(new MigLayout());

	protected abstract void update();

	public Panel getPanel() {
		return pnlSurface;
	}
	
	// instance initializer
	{
		InputManager.resetEventHandlers();
	}

	public void finish() {
		pnlSurface.removeAll();
		pnlSurface.setEnabled(false);
		pnlSurface.setVisible(false);
		pnlSurface = null;
	}
}
