package unnamed_platformer.view.gui.dialogs;

import java.awt.Frame;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.view.gui.GUIManager;

public class Dialog_KeyAssignment extends Dialog
{
	private static final long serialVersionUID = 1396852545275302025L;
	final JLabel lblMessage = new JLabel("Press a key");

	public Dialog_KeyAssignment(Frame owner) {
		super(owner, "Key Assignment");

		// SETUP LABEL
		lblMessage.setForeground(GUIManager.COLOR_WHITE);
		lblMessage.setBorder(new EmptyBorder(8, 8, 8, 8));

		this.add(lblMessage);

		this.pack();
		this.setLocationRelativeTo(owner);
	}

	@Override
	public void update() {

		// LWJGL JInput Keys
		List<Integer> keyCodes = InputManager.getRawKeyPresses();

		
		if (keyCodes.isEmpty()) {
			return;
		}

		if (keyCodes.size() > 2) {
			System.out.println("> 2");
			return;
		}

		lblMessage.setText(String.valueOf(keyCodes.get(0)));

		// Dialog_KeyAssignment.this.setVisible(false);
	}

}
