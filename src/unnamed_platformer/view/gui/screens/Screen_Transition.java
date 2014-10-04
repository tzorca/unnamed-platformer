package unnamed_platformer.view.gui.screens;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import unnamed_platformer.game.other.World;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

public class Screen_Transition extends BaseScreen_GUI
{
	public Screen_Transition() {
		super();

		Label lblInfo;
		JButton btnNext;

		// this will already have been set by the end level object
		int nextLevelIndex = World.getCurrentLevelIndex();

		if (World.hasLevelIndex(nextLevelIndex)) {
			lblInfo = new Label("Level "
					+ String.valueOf(World.getCurrentLevelIndex() + 1));

			btnNext = new JButton("Start");
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
		lblInfo.setFont(GUIManager.HEADING_FONT);
		lblInfo.setForeground(GUIManager.COLOR_LIGHT_GREY);
		btnNext.setFont(GUIManager.SUB_HEADING_FONT);
		GUIHelper.styleButton(btnNext, 12);

		pnlSurface.add(lblInfo, GUIManager.CENTER_LAYOUT + ", gaptop 10%, ");
		pnlSurface.add(btnNext, GUIManager.CENTER_LAYOUT + ", pushy");
	}
}
