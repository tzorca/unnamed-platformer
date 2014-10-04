package unnamed_platformer.view.gui.dialogs;

import java.awt.Frame;

import javax.swing.JDialog;

import unnamed_platformer.view.gui.GUIManager;

public class Dialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	public void update() {
	}

	public Dialog(Frame owner) {
		super(owner, false);
		getContentPane().setBackground(GUIManager.COLOR_DARK_BLUE_3);
	}

	public Dialog(Frame owner, String title) {
		super(owner, title, false);
		getContentPane().setBackground(GUIManager.COLOR_DARK_BLUE_3);
	}
}
