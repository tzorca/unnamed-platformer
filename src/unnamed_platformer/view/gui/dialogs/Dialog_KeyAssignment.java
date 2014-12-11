package unnamed_platformer.view.gui.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.GUIManager;

public class Dialog_KeyAssignment extends Dialog
{
	private static final long serialVersionUID = 1396852545275302025L;

	public Dialog_KeyAssignment(Frame owner, final ParamRunnable callback) {
		super(owner, "New Key");

		final JLabel lblMessage = new JLabel("Press a key");
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setFont(GUIManager.FONT_NORMAL);
		lblMessage.setBorder(new EmptyBorder(24, 32, 24, 32));

		this.setUndecorated(true);
		this.add(lblMessage);

		this.pack();
		this.setLocationRelativeTo(owner);

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				callback.run(e.getKeyCode());
				Dialog_KeyAssignment.this.setVisible(false);
			}
		});
	}
}
