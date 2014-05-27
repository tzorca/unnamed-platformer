package unnamed_platformer.gui;

import java.awt.Panel;

import net.miginfocom.swing.MigLayout;

public abstract class ZScreen {
	protected Panel panel = new Panel(new MigLayout());

	protected abstract void update();

	public Panel getPanel() {
		return panel;
	}
}
