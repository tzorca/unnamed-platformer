package unnamed_platformer.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.app.App;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.App.State;
import unnamed_platformer.game.parameters.GUIRef;
import unnamed_platformer.game.parameters.Ref;

public class Screen_Start extends BaseScreen_GUI {

	// Instance initializer
	{
		JLabel lblGameTitle = new JLabel(Ref.APP_TITLE);
		lblGameTitle.setFont(GUIRef.HEADING_FONT);
		lblGameTitle.setForeground(Color.darkGray);

		JButton btnDynamicWorld = new JButton("Dynamic World");
		btnDynamicWorld.setFont(GUIRef.SUB_HEADING_FONT);
		btnDynamicWorld.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnDynamicWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameManager.playRandomGame();
				App.state = State.Play;
			}
		});
		

		JButton btnCustomWorlds = new JButton("Custom Worlds");
		btnCustomWorlds.setFont(GUIRef.SUB_HEADING_FONT);
		btnCustomWorlds.setBorder(new EmptyBorder(15, 20, 15, 20));
		btnCustomWorlds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				App.state = State.SelectWorld;
			}
		});

		panel.add(lblGameTitle, GUIRef.CENTER_LAYOUT + ", gapbottom 20%, ");
		panel.add(btnDynamicWorld, GUIRef.CENTER_LAYOUT);
		panel.add(btnCustomWorlds, GUIRef.CENTER_LAYOUT);

	}

}
