package unnamed_platformer.view.dialogs;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JLabel;

import unnamed_platformer.app.Main;
import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.RawKey;
import unnamed_platformer.view.GUIHelper.ParamRunnable;

public class Dialog_KeyAssignment extends Dialog
{
	private static final long serialVersionUID = 1396852545275302025L;

	private RawKey lastKeyboardKey;
	private ParamRunnable callback;

	public Dialog_KeyAssignment(Frame owner, final ParamRunnable callback) {
		super(owner, "New Key");

		final JLabel lblMessage = new JLabel("Press a key");
		StyleGlobals.STYLE_PRESS_KEY_MESSAGE.apply(lblMessage);

		this.setUndecorated(true);
		this.add(lblMessage);

		this.pack();
		this.setLocationRelativeTo(owner);

		this.callback = callback;

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				lastKeyboardKey = RawKey.fromAWTKeyCode(e.getKeyCode());
			}
		});

		Main.setHotKeysAllowed(false);
	}

	@Override
	public void update() {
		RawKey pressedKey = lastKeyboardKey;

		// Prefer gamepad over keyboard keys if both pressed at the same time
		// (LWJGL won't be sending keyboard keys while not focused on canvas)
		List<RawKey> gamepadKeys = InputManager.getPessedRawKeys();
		if (!gamepadKeys.isEmpty()) {
			pressedKey = gamepadKeys.get(0);
		}
		
		if (pressedKey != null) {
			InputManager.resetEvents();
			callback.run(pressedKey);
			Dialog_KeyAssignment.this.setVisible(false);
		}

	}
}
