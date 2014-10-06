package unnamed_platformer.view.gui.screens;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import unnamed_platformer.game.other.World;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.google.common.collect.Lists;

public class Screen_Transition extends BaseScreen_GUI
{

	JButton btnNext = new JButton("Start");

	public Screen_Transition() {
		super();

		Label lblInfo;

		// This will already have been set by the end level object
		int nextLevelIndex = World.getCurrentLevelIndex();

		if (World.hasLevelIndex(nextLevelIndex)) {
			lblInfo = new Label("Level "
					+ String.valueOf(World.getCurrentLevelIndex() + 1));

			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GUIManager.changeScreen(ScreenType.Play);
				}
			});
		} else {
			lblInfo = new Label("You finished all the levels.");

			btnNext = new JButton("Ok");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GUIManager.changeScreen(ScreenType.SelectWorld);
				}
			});
		}

		pnlSurface.setBackground(GUIManager.COLOR_DARK_BLUE_2);
		lblInfo.setFont(GUIManager.FONT_HEADING);
		lblInfo.setForeground(GUIManager.COLOR_LIGHT_GREY);
		btnNext.setFont(GUIManager.FONT_SUB_HEADING);
		GUIHelper.styleButton(btnNext, 6);

		pnlSurface.add(lblInfo, GUIManager.CENTER_LAYOUT + ", gaptop 10%, ");
		pnlSurface.add(btnNext, GUIManager.CENTER_LAYOUT + ", pushy");

		GUIHelper.addDirectionalNavigation(Lists.newArrayList(btnNext), null);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				btnNext.requestFocus();
			}
		});
	}

	public void update() {
		super.update();

		// Default button selection
		if (!btnNext.hasFocus()) {
			btnNext.requestFocusInWindow();
		}
	}
}
