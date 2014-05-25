package unnamed_platformer.gui;

import javax.swing.JPanel;

import unnamed_platformer.app.ViewManager;

public abstract class Screen {
	protected JPanel panel = new JPanel();

	protected abstract void update();

	protected void activate() {
		ViewManager.setGUIPanel(panel);
	}
}
