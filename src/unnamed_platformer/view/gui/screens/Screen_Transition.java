package unnamed_platformer.view.gui.screens;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.InputManager.PlrGameKey;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

public class Screen_Transition extends BaseScreen_GUI
{
	JLabel lblInfo, lblTimer = new JLabel();

	private long timeStarted;
	private ScreenType nextScreen;

	private final int TRANSITION_SECONDS = 3;

	public Screen_Transition() {
		super();

		timeStarted = TimeManager.time();
		lblTimer.setText(String.valueOf(TRANSITION_SECONDS));

		int nextLevelIndex = World.getCurrentLevelIndex();

		if (World.hasLevelIndex(nextLevelIndex)) {
			// Play a sound for completing the level!
			AudioManager.playSample("level-complete");

			lblInfo = new JLabel("Next: Level "
					+ String.valueOf(nextLevelIndex + 1));
			nextScreen = ScreenType.Play;
		} else {
			lblInfo = new JLabel("You finished all the levels! Good job!");
			nextScreen = ScreenType.SelectWorld;
		}

		StyleGlobals.STYLE_HEADING.apply(lblInfo);
		StyleGlobals.STYLE_HEADING.apply(lblTimer);

		pnlSurface.add(lblInfo, StyleGlobals.CENTER_LAYOUT + ", gaptop 10%, ");
		pnlSurface.add(lblTimer, StyleGlobals.CENTER_LAYOUT + ", gaptop 10%, ");

		pnlSurface.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Collection<PlrGameKey> plrGameKeys = InputManager
						.getGameKeysMatchingKeyEvent(e);
				processGameKeys(plrGameKeys);

			}

		});
	}

	private void processGameKeys(Collection<PlrGameKey> plrGameKeys) {
		for (PlrGameKey plrGameKey : plrGameKeys) {
			switch (plrGameKey.getGameKey()) {
			case A:
				GUIManager.changeScreen(nextScreen);
				break;
			case B:
				// TODO
				break;
			default:
				break;
			}
		}
	}

	public void update() {
		super.update();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pnlSurface.requestFocusInWindow();
			}
		});
		
		processGameKeys(InputManager.getPressedGameKeys());

		int timeElapsed = (int) TimeManager.secondsSince(timeStarted);
		int timeLeft = TRANSITION_SECONDS - timeElapsed;

		lblTimer.setText(String.valueOf(timeLeft));

		if (timeLeft <= 0) {
			GUIManager.changeScreen(nextScreen);
		}
	}
}
