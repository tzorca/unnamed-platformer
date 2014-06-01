package unnamed_platformer.gui;

import java.awt.Panel;

import net.miginfocom.swing.MigLayout;

public abstract class ZScreen {
	protected Panel panel = new Panel(new MigLayout());

	protected abstract void update();

	public Panel getPanel() {
		return panel;
	}

	public void finish() {
		panel.removeAll();
		panel.setIgnoreRepaint(true);
		panel.setEnabled(false);
		panel.setVisible(false);
		panel = null;
	}
}
