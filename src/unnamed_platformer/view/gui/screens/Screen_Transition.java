package unnamed_platformer.view.gui.screens;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.PlrGameKey;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.res_mgt.SoundManager;
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
			SoundManager.playSample("level-complete");

			lblInfo = new JLabel("Next: Level "
					+ String.valueOf(nextLevelIndex + 1));
			nextScreen = ScreenType.Play;
		} else {
			lblInfo = new JLabel("You finished all the levels! Good job!");
			nextScreen = ScreenType.SelectWorld;
		}

		StyleRef.STYLE_HEADING.apply(lblInfo);
		StyleRef.STYLE_HEADING.apply(lblTimer);

		pnlSurface.add(lblInfo, StyleRef.CENTER_LAYOUT + ", gaptop 10%, ");
		pnlSurface.add(lblTimer, StyleRef.CENTER_LAYOUT + ", gaptop 10%, ");

		pnlSurface.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Collection<PlrGameKey> plrGameKeys = InputManager
						.getGameKeysMatchingKeyEvent(e);
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
		});
	}

	public void update() {
		super.update();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pnlSurface.requestFocusInWindow();
			}
		});

		int timeElapsed = (int) TimeManager.secondsSince(timeStarted);
		int timeLeft = TRANSITION_SECONDS - timeElapsed;

		lblTimer.setText(String.valueOf(timeLeft));

		if (timeLeft <= 0) {
			GUIManager.changeScreen(nextScreen);
		}
	}
}
