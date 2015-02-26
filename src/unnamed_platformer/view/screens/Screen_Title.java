package unnamed_platformer.view.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import unnamed_platformer.app.Main;
import unnamed_platformer.globals.AppGlobals;
import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.view.GUIHelper;
import unnamed_platformer.view.GUIManager;
import unnamed_platformer.view.GUIManager.ScreenType;

import com.google.common.collect.Lists;

public class Screen_Title extends BaseScreen_GUI
{
	List<JButton> buttons;

	private int buttonIndex = 0;

	public Screen_Title() {
		super();

		pnlSurface.setBackground(StyleGlobals.COLOR_MAIN_PLUS);

		JLabel lblGameTitle = new JLabel(AppGlobals.TITLE);
		StyleGlobals.STYLE_HEADING.apply(lblGameTitle);

		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIManager.changeScreen(ScreenType.SelectWorld);
			}
		});

		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIManager.changeScreen(ScreenType.Options);
			}
		});

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.doHalt();
			}
		});

		buttons = Lists.newArrayList(btnPlay, btnOptions, btnExit);
		for (JButton btn : buttons) {
			StyleGlobals.STYLE_NORMAL_BUTTON.apply(btn);
			btn.addFocusListener(new btn_FocusListener());
		}

		GUIHelper.addDirectionalNavigation(buttons, null, true);

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblGameTitle, StyleGlobals.CENTER_LAYOUT
				+ ", gapbottom 40%, ");
		pnlSurface.add(btnPlay, StyleGlobals.CENTER_LAYOUT);
		pnlSurface.add(btnOptions, StyleGlobals.CENTER_LAYOUT);
		pnlSurface.add(btnExit, StyleGlobals.CENTER_LAYOUT);

		GUIHelper.toWidest(buttons);
	}

	private void changeButton(int delta) {
		int newIndex = buttonIndex + delta;
		if (newIndex >= buttons.size()) {
			newIndex = 0;
		}
		if (newIndex < 0) {
			newIndex = buttons.size() - 1;
		}

		buttonIndex = newIndex;
		buttons.get(buttonIndex).requestFocusInWindow();
	}

	@Override
	public void update() {
		super.update();

		// LWJGL JInput Keys
		if (InputManager.keyPressOccurred(GameKey.UP, 1)) {
			changeButton(-1);
		}
		if (InputManager.keyPressOccurred(GameKey.DOWN, 1)) {
			changeButton(1);
		}
		if (InputManager.keyPressOccurred(GameKey.A, 1)) {
			JButton btn = buttons.get(buttonIndex);
			if (btn != null) {
				btn.doClick();
			}
		}

		// Default button selection
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!buttons.get(buttonIndex).hasFocus()) {
					buttons.get(buttonIndex).requestFocusInWindow();

				}
			}
		});
	}

	private class btn_FocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e) {
			for (int index = 0; index < buttons.size(); index++) {
				JButton btn = buttons.get(index);
				if (btn.equals(e.getSource())) {
					buttonIndex = index;
				}
			}
		}

		public void focusLost(FocusEvent e) {
		}
	}

}
