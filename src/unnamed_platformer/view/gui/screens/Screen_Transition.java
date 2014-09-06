package unnamed_platformer.view.gui.screens;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import unnamed_platformer.game.other.World;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;

public class Screen_Transition extends BaseScreen_GUI
{
	// Instance initializer
	{
		pnlSurface.setBackground(GUIManager.GUI_BG_COLOR);

		Label lblNextLevel = new Label("Level "
				+ String.valueOf(World.getCurrentLevelIndex() + 1));
		lblNextLevel.setFont(GUIManager.HEADING_FONT);
		lblNextLevel.setForeground(GUIManager.GUI_FG_COLOR);

		JButton btnStart = new JButton("Start");
		btnStart.setFont(GUIManager.SUB_HEADING_FONT);
		GUIHelper.styleButton(btnStart, 12);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (GUIManager.getPreviousScreen()) {
				case Edit:
				case Play:
					GUIManager.back();
					break;
				case SelectWorld:
				case Start:
				case Transition:
				default:
					// ?
					break;
				}
			}
		});
		
		
		pnlSurface.add(lblNextLevel, GUIManager.CENTER_LAYOUT
				+ ", gapbottom 20%, ");
		pnlSurface.add(btnStart, GUIManager.CENTER_LAYOUT);

	}
}
