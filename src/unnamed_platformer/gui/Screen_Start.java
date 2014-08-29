package unnamed_platformer.gui;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIManager.ScreenType;

public class Screen_Start extends BaseScreen_GUI {

	// Instance initializer
	{
		Label lblGameTitle = new Label(Ref.APP_TITLE);
		lblGameTitle.setFont(GUIManager.HEADING_FONT);
		lblGameTitle.setForeground(Color.darkGray);

		JButton btnDynamicWorld = new JButton("Dynamic World");
		btnDynamicWorld.setFont(GUIManager.SUB_HEADING_FONT);
		btnDynamicWorld.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnDynamicWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				World.playRandomGame();
			}
		});
		

		JButton btnCustomWorlds = new JButton("Custom Worlds");
		btnCustomWorlds.setFont(GUIManager.SUB_HEADING_FONT);
		btnCustomWorlds.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnCustomWorlds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIManager.changeScreen(ScreenType.SelectWorld);
			}
		});

		pnlSurface.add(lblGameTitle, GUIManager.CENTER_LAYOUT + ", gapbottom 20%, ");
		pnlSurface.add(btnDynamicWorld, GUIManager.CENTER_LAYOUT);
		pnlSurface.add(btnCustomWorlds, GUIManager.CENTER_LAYOUT);

	}

}
