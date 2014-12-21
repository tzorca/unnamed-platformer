package unnamed_platformer.view.gui.screens;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.res_mgt.SoundManager;
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
			// Play a sound for completing the level!
			SoundManager.playSample("level-complete");
			
			lblInfo = new Label("Level "
					+ String.valueOf(World.getCurrentLevelIndex() + 1));

			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GUIManager.changeScreen(ScreenType.Play);
				}
			});
		} else {
			lblInfo = new Label("You finished all the levels! Good job!");

			btnNext = new JButton("Ok");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GUIManager.changeScreen(ScreenType.SelectWorld);
				}
			});
		}

		lblInfo.setFont(StyleRef.FONT_HEADING);
		lblInfo.setForeground(StyleRef.COLOR_LIGHT_GREY);
		btnNext.setFont(StyleRef.FONT_SUB_HEADING);
		GUIHelper.styleButton(btnNext, 6, StyleRef.COLOR_MAIN_PLUS);

		pnlSurface.add(lblInfo, StyleRef.CENTER_LAYOUT + ", gaptop 10%, ");
		pnlSurface.add(btnNext, StyleRef.CENTER_LAYOUT + ", pushy");

		GUIHelper.addDirectionalNavigation(Lists.newArrayList(btnNext), null);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				btnNext.requestFocusInWindow();
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
