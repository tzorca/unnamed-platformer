package unnamed_platformer.view.dialogs;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;

import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.InputManager;

public class Dialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	public void update() {
	}

	public Dialog(Frame owner) {
		super(owner, false);
		InputManager.resetEvents();
		initStandardProperties();
	}

	public Dialog(Frame owner, String title) {
		super(owner, title, false);
		InputManager.resetEvents();
		initStandardProperties();
	}

	private void initStandardProperties() {

		getContentPane().setBackground(StyleGlobals.COLOR_MAIN_PLUS);

		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {
				Dialog.this.setVisible(false);
			}

			public void windowGainedFocus(WindowEvent e) {
			}
		});
	}
}
