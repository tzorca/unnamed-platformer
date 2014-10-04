package unnamed_platformer.view.gui.dialogs;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.game.other.Editor;
import unnamed_platformer.game.other.World;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.screens.Screen_Edit;

import com.google.common.collect.Lists;

public class Dialog_EditMenu extends Dialog
{
	private static final long serialVersionUID = -846612117309570380L;

	private static final ImageIcon /* */
	/*    */IMG_EDIT_MODE = ImageHelper.getImageIconContent("gui_modeEdit"),
			IMG_PLAY_MODE = ImageHelper.getImageIconContent("gui_modePlay"),
			IMG_ADD = ImageHelper.getImageIconContent("gui_add"),
			IMG_NEXT = ImageHelper.getImageIconContent("gui_next"),
			IMG_PREV = ImageHelper.getImageIconContent("gui_prev"),
			IMG_REMOVE = ImageHelper.getImageIconContent("gui_remove"),
			IMG_SAVE = ImageHelper.getImageIconContent("gui_save");

	private final JButton btnModeSwitch = new JButton(IMG_PLAY_MODE);
	private final JButton btnPrevLevel = new JButton(IMG_PREV);
	private final JButton btnAddLevel = new JButton(IMG_ADD);
	private final JButton btnNextLevel = new JButton(IMG_NEXT);
	private final JButton btnRemoveLevel = new JButton(IMG_REMOVE);
	private final JButton btnSaveLevel = new JButton(IMG_SAVE);

	private Editor editor;
	private Screen_Edit screenEdit;
	private final JLabel lblCurrentLevel = new JLabel();

	public Dialog_EditMenu(Frame owner, Editor editor, Screen_Edit screenEdit) {
		super(owner, "Editor Options");
		this.editor = editor;
		this.screenEdit = screenEdit;

		this.setLayout(new MigLayout());
		this.setLocationRelativeTo(owner);

		// SETUP COMPONENTS
		btnPrevLevel.addActionListener(new btnPrevLevel_Click());
		btnNextLevel.addActionListener(new btnNextLevel_Click());
		btnAddLevel.addActionListener(new btnAddLevel_Click());
		btnRemoveLevel.addActionListener(new btnRemoveLevel_Click());
		btnSaveLevel.addActionListener(new btnSaveLevel_Click());
		btnModeSwitch.addActionListener(new btnModeSwitch_Click());
		GUIHelper.removeButtonPadding(Lists.newArrayList(btnPrevLevel,
				btnNextLevel, btnAddLevel, btnRemoveLevel, btnSaveLevel,
				btnModeSwitch));
		GUIHelper.styleButtons(Lists.newArrayList(btnPrevLevel, btnNextLevel,
				btnAddLevel, btnRemoveLevel, btnSaveLevel, btnModeSwitch), 0);
		lblCurrentLevel.setText("Level "
				+ String.valueOf(World.getCurrentLevelIndex() + 1));
		lblCurrentLevel.setForeground(GUIManager.COLOR_WHITE);
		lblCurrentLevel.setBorder(new EmptyBorder(8, 8, 8, 8));

		// ADD COMPONENTS
		final boolean currentlyEditing = !World.playing();
		this.add(btnModeSwitch);
		if (currentlyEditing) {
			this.add(btnPrevLevel);
			this.add(btnNextLevel);
			this.add(btnAddLevel);
			this.add(btnRemoveLevel);
			this.add(btnSaveLevel);
		} else {
			btnModeSwitch.setIcon(IMG_EDIT_MODE);
		}
		this.add(lblCurrentLevel);

		// CREATE MENU EXIT RUNNABLE
		Runnable exitRunnable = new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Dialog_EditMenu.this.setVisible(false);
						ViewManager.focusRenderCanvas();
					}
				});
			}
		};

		// ADD DIRECTIONAL NAVIGATION
		GUIHelper.addDirectionalNavigation(Lists.newArrayList(btnModeSwitch,
				btnPrevLevel, btnNextLevel, btnAddLevel, btnRemoveLevel,
				btnSaveLevel), exitRunnable);

		this.pack();
	}

	private class btnModeSwitch_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dialog_EditMenu.this.setVisible(false);
					screenEdit.toggleEditMode();
					ViewManager.focusRenderCanvas();
				}
			});
		}
	}

	private class btnAddLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dialog_EditMenu.this.setVisible(false);
					World.addBlankLevel();
					editor.changeLevel(World.getLevelCount() - 1);
					ViewManager.focusRenderCanvas();
				}
			});
		}
	}

	private class btnNextLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dialog_EditMenu.this.setVisible(false);
					editor.levelInc(1);
					ViewManager.focusRenderCanvas();
				}
			});
		}
	}

	private class btnPrevLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dialog_EditMenu.this.setVisible(false);
					editor.levelInc(-1);
					ViewManager.focusRenderCanvas();
				}

			});
		}
	}

	private class btnRemoveLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dialog_EditMenu.this.setVisible(false);
					editor.removeLevel();
					ViewManager.focusRenderCanvas();
				}
			});

		}
	}

	private class btnSaveLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dialog_EditMenu.this.setVisible(false);
					editor.save();
					ViewManager.focusRenderCanvas();
				}
			});
		}
	}

}
