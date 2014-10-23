package unnamed_platformer.view.gui.screens;

import java.awt.Color;
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
import unnamed_platformer.app.Settings;
import unnamed_platformer.app.InputManager.GameKey;
import unnamed_platformer.app.Settings.SettingName;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.google.common.collect.Lists;

public class Screen_Title extends BaseScreen_GUI
{
	JButton btnEdit = new JButton("Edit");
	JButton btnPlay = new JButton("Play");
	JButton btnOptions = new JButton("Options");

	List<JButton> buttons = Lists.newArrayList(btnPlay, btnEdit, btnOptions);

	private int buttonIndex = 0;

	public Screen_Title() {
		super();

		Label lblGameTitle = new Label(Ref.APP_TITLE);
		lblGameTitle.setFont(GUIManager.FONT_HEADING);
		lblGameTitle.setForeground(GUIManager.COLOR_LIGHT_GREY);

		btnPlay.setFont(GUIManager.FONT_SUB_HEADING);
		btnPlay.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String officialLevelsetName = Settings
						.getString(SettingName.OFFICIAL_LEVELSET_NAME);
				if (officialLevelsetName != null
						&& !officialLevelsetName.isEmpty()) {
					World.load(officialLevelsetName);
					GUIManager.changeScreen(ScreenType.Play);
				}
			}
		});

		btnEdit.setFont(GUIManager.FONT_SUB_HEADING);
		btnEdit.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIManager.changeScreen(ScreenType.SelectWorld);
			}
		});

		btnOptions.setFont(GUIManager.FONT_SUB_HEADING);
		btnOptions.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIManager.changeScreen(ScreenType.Options);
			}
		});

		for (JButton btn : buttons) {
			btn.addFocusListener(new btn_FocusListener());
			btn.setFont(GUIManager.FONT_SUB_HEADING);
			btn.setIconTextGap(0);
		}
		GUIHelper.styleButton(btnPlay, 10, Color.GREEN.darker());
		GUIHelper.styleButton(btnEdit, 10, GUIManager.COLOR_ORANGE);
		GUIHelper.styleButton(btnOptions, 10, Color.GRAY.darker());

		GUIHelper.addDirectionalNavigation(buttons, null, true);

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblGameTitle, GUIManager.CENTER_LAYOUT
				+ ", gapbottom 20%, ");
		pnlSurface.add(btnPlay, GUIManager.CENTER_LAYOUT);
		pnlSurface.add(btnEdit, GUIManager.CENTER_LAYOUT);
		pnlSurface.add(btnOptions, GUIManager.CENTER_LAYOUT);

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
		buttons.get(buttonIndex).requestFocus();
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
					buttons.get(buttonIndex).requestFocus();

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
