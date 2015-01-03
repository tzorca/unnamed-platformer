package unnamed_platformer.view.gui.dialogs;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;

import unnamed_platformer.globals.StyleRef;

public class Dialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	public void update() {
	}

	public Dialog(Frame owner) {
		super(owner, false);
		initStandardProperties();
	}

	public Dialog(Frame owner, String title) {
		super(owner, title, false);
		initStandardProperties();
	}

	private void initStandardProperties() {

		getContentPane().setBackground(StyleRef.COLOR_MAIN_PLUS);

		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {
				Dialog.this.setVisible(false);
			}

			public void windowGainedFocus(WindowEvent e) {
			}
		});
	}
}
