package unnamed_platformer.gui;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.app.Main;
import unnamed_platformer.app.Main.State;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.globals.GUIRef;
import unnamed_platformer.globals.Ref;

public class Screen_Start extends BaseScreen_GUI {

	// Instance initializer
	{
		Label lblGameTitle = new Label(Ref.APP_TITLE);
		lblGameTitle.setFont(GUIRef.HEADING_FONT);
		lblGameTitle.setForeground(Color.darkGray);

		JButton btnDynamicWorld = new JButton("Dynamic World");
		btnDynamicWorld.setFont(GUIRef.SUB_HEADING_FONT);
		btnDynamicWorld.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnDynamicWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameManager.playRandomGame();
			}
		});
		

		JButton btnCustomWorlds = new JButton("Custom Worlds");
		btnCustomWorlds.setFont(GUIRef.SUB_HEADING_FONT);
		btnCustomWorlds.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnCustomWorlds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.state = State.SelectWorld;
			}
		});

		pnlSurface.add(lblGameTitle, GUIRef.CENTER_LAYOUT + ", gapbottom 20%, ");
		pnlSurface.add(btnDynamicWorld, GUIRef.CENTER_LAYOUT);
		pnlSurface.add(btnCustomWorlds, GUIRef.CENTER_LAYOUT);

	}

}
