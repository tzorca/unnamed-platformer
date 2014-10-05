package unnamed_platformer.view.gui.screens;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

public class Screen_OldStart extends BaseScreen_GUI {

	public Screen_OldStart() {
		super(); 

		Label lblGameTitle = new Label(Ref.APP_TITLE);
		lblGameTitle.setFont(GUIManager.FONT_HEADING);
		lblGameTitle.setForeground(Color.darkGray);

		JButton btnDynamicWorld = new JButton("Dynamic World");
		btnDynamicWorld.setFont(GUIManager.FONT_SUB_HEADING);
		btnDynamicWorld.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnDynamicWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				World.playRandomGame();
			}
		});
		

		JButton btnCustomWorlds = new JButton("Custom Worlds");
		btnCustomWorlds.setFont(GUIManager.FONT_SUB_HEADING);
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
