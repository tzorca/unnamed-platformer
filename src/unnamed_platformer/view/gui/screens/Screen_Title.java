package unnamed_platformer.view.gui.screens;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.GameKey;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.Settings;
import unnamed_platformer.app.Settings.SettingName;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.google.common.collect.Lists;

public class Screen_Title extends BaseScreen_GUI
{
	List<JButton> buttons;

	private int buttonIndex = 0;

	public Screen_Title() {
		super();

		pnlSurface.setBackground(StyleRef.COLOR_MAIN_PLUS);

		Label lblGameTitle = new Label(Ref.APP_TITLE);
		lblGameTitle.setFont(StyleRef.FONT_HEADING);
		lblGameTitle.setForeground(StyleRef.COLOR_LIGHT_GREY);

		JButton btnPlay = new JButton("Play");
		btnPlay.setFont(StyleRef.FONT_SUB_HEADING);
		btnPlay.setBorder(new EmptyBorder(15, 20, 15, 20));
		GUIHelper.styleButton(btnPlay, 10, StyleRef.COLOR_MAIN);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String officialLevelsetName = Settings
						.getString(SettingName.OFFICIAL_LEVELSET_NAME);
				if (officialLevelsetName != null
						&& !officialLevelsetName.isEmpty()) {
					World.load(officialLevelsetName);
					GUIManager.changeScreen(ScreenType.SelectWorld);
				}
			}
		});

		JButton btnOptions = new JButton("Options");
		btnOptions.setFont(StyleRef.FONT_SUB_HEADING);
		btnOptions.setBorder(new EmptyBorder(15, 20, 15, 20));
		GUIHelper.styleButton(btnOptions, 10, StyleRef.COLOR_MAIN);
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIManager.changeScreen(ScreenType.Options);
			}
		});

		JButton btnExit = new JButton("Exit");
		btnOptions.setFont(StyleRef.FONT_SUB_HEADING);
		btnOptions.setBorder(new EmptyBorder(15, 20, 15, 20));
		GUIHelper.styleButton(btnExit, 10, StyleRef.COLOR_MAIN);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.doExit();
			}
		});

		buttons = Lists.newArrayList(btnPlay, btnOptions, btnExit);
		for (JButton btn : buttons) {
			btn.addFocusListener(new btn_FocusListener());
			btn.setFont(StyleRef.FONT_NORMAL);
			btn.setIconTextGap(0);
		}

		GUIHelper.addDirectionalNavigation(buttons, null, true);

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblGameTitle, StyleRef.CENTER_LAYOUT
				+ ", gapbottom 40%, ");
		pnlSurface.add(btnPlay, StyleRef.CENTER_LAYOUT);
		pnlSurface.add(btnOptions, StyleRef.CENTER_LAYOUT);
		pnlSurface.add(btnExit, StyleRef.CENTER_LAYOUT);

		GUIHelper.toWidest(buttons);

		// Default button selection
		if (!btnPlay.hasFocus()) {
			btnPlay.requestFocusInWindow();
		}
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
