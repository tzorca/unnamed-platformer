package gui;

import javax.swing.JPanel;

public abstract class A_GUI_Screen extends JPanel {
	private static final long serialVersionUID = -7495190484102556669L;

	public abstract void update();

	public abstract void onStart();
}
