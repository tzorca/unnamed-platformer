package unnamed_platformer.view.gui.screens;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import unnamed_platformer.app.FileHelper;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.PlayerGameKey;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.InputRef.GameKey;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.google.common.collect.Lists;

public class Screen_SelectWorld extends BaseScreen_GUI
{
	DefaultListModel<String> mdlWorlds = new DefaultListModel<String>();

	// Instantiate GUI components
	JLabel lblTitle = new JLabel("Custom Worlds");
	JList<String> lstWorlds = new JList<String>();
	JPanel pnlButtons = new JPanel();
	JButton btnNew = new JButton("New");
	JButton btnPlay = new JButton("Play");
	JButton btnRename = new JButton("Rename");
	JButton btnEdit = new JButton("Edit");
	JButton btnCopy = new JButton("Copy");
	JButton btnDelete = new JButton("Delete");

	// Collect buttons
	List<JButton> buttons =Lists.newArrayList(btnPlay, btnEdit, btnNew,
			btnRename, btnCopy, btnDelete);

	// Collect all components
	@SuppressWarnings("unchecked")
	List<? extends JComponent> components = Lists.newArrayList(lstWorlds,
			btnNew, btnPlay, btnRename, btnEdit, btnCopy, btnDelete);

	public Screen_SelectWorld() {
		super();

		// SETUP TITLE
		JLabel lblTitle = new JLabel("Select a World");
		lblTitle.setFont(GUIManager.FONT_SUB_HEADING);
		lblTitle.setForeground(GUIManager.COLOR_LIGHT_GREY);

		// SETUP WORLD LIST
		for (String worldName : ResManager.list(World.class, true)) {
			mdlWorlds.addElement(worldName);
		}
		lstWorlds.setModel(mdlWorlds);
		lstWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstWorlds.setFont(GUIManager.FONT_NORMAL);
		lstWorlds.setBackground(GUIManager.COLOR_DARK_BLUE_2);
		lstWorlds.setForeground(GUIManager.COLOR_WHITE);
		lstWorlds.setSelectionBackground(GUIManager.COLOR_HIGHLIGHT_BLUE);
		lstWorlds.addListSelectionListener(new lstWorlds_SelectionListener());
		lstWorlds.setSelectedIndex(0);

		// SETUP WORLD LIST SCROLL PANE
		JScrollPane scrlWorlds = new JScrollPane(lstWorlds);
		GUIHelper.styleComponentColors(scrlWorlds, false);
		GUIHelper.styleBorder(scrlWorlds, 0, false);
		scrlWorlds.getViewport().setBackground(GUIManager.COLOR_DARK_BLUE_2);

		// SETUP BUTTONS
		btnNew.addActionListener(new btnNew_Click());
		btnPlay.addActionListener(new btnPlay_Click());
		btnRename.addActionListener(new btnRename_Click());
		btnEdit.addActionListener(new btnEdit_Click());
		btnCopy.addActionListener(new btnCopy_Click());
		btnDelete.addActionListener(new btnDelete_Click());
		for (JButton btn : buttons) {
			btn.addFocusListener(new btn_FocusListener());
			btn.setFont(GUIManager.FONT_NORMAL);
		}
		GUIHelper.styleButtons(buttons, 6);

		// ADD GLOBAL LISTENER FOR ARROW NAVIGATION
		for (JComponent c : components) {
			c.addKeyListener(new Global_KeyListener());
		}

		// ADD BUTTONS TO PANEL
		pnlButtons.add(btnPlay);
		pnlButtons.add(btnEdit);
		pnlButtons.add(Box.createRigidArea(new Dimension(16, 0)));
		pnlButtons.add(btnNew);
		pnlButtons.add(Box.createRigidArea(new Dimension(16, 0)));
		pnlButtons.add(btnRename);
		pnlButtons.add(btnCopy);
		pnlButtons.add(btnDelete);

		// SETUP BUTTON PANEL
		pnlButtons.setBackground(GUIManager.COLOR_DARK_BLUE_3);

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblTitle, "gapx 8px 8px, pushx, wrap");
		pnlSurface.add(scrlWorlds, "gapx 8px 8px, gapy 16px, grow, pushy, wrap");
		pnlSurface.setBackground(GUIManager.COLOR_DARK_BLUE_3);
		pnlSurface.add(pnlButtons, "gapx 8px 8px, gapy 4px");
	}

	private int buttonIndex = 0;
	private String gameName;

	// ================================================================
	// NAVIGATION
	// ================================================================

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

	private void changeListSelectedIndex(int delta) {
		int newIndex = lstWorlds.getSelectedIndex() + delta;
		if (newIndex >= mdlWorlds.size()) {
			newIndex = 0;
		}
		if (newIndex < 0) {
			newIndex = mdlWorlds.size() - 1;
		}

		lstWorlds.setSelectedIndex(newIndex);

		buttons.get(buttonIndex).requestFocus();
	}
	// =================================================================
	// EVENTS
	// =================================================================

	public void update() {
		super.update();

		// LWJGL JInput Keys
		if (InputManager.keyPressOccurred(GameKey.LEFT, 1)) {
			changeButton(-1);
		}
		if (InputManager.keyPressOccurred(GameKey.RIGHT, 1)) {
			changeButton(1);
		}
		if (InputManager.keyPressOccurred(GameKey.UP, 1)) {
			changeListSelectedIndex(-1);
		}
		if (InputManager.keyPressOccurred(GameKey.DOWN, 1)) {
			changeListSelectedIndex(1);
		}
		if (InputManager.keyPressOccurred(GameKey.A, 1)) {
			JButton btn = buttons.get(buttonIndex);
			if (btn != null) {
				btn.doClick();
			}
		}
		
		// Default button selection
		if (!buttons.get(buttonIndex).hasFocus()) {
			buttons.get(buttonIndex).requestFocusInWindow();
		}
	}

	// Java VK KeyEvents
	private class Global_KeyListener extends KeyAdapter
	{
		public void keyPressed(KeyEvent e) {
			Collection<PlayerGameKey> plrGameKeys = InputManager
					.getGameKeysMatchingKeyEvent(e);

			for (PlayerGameKey plrGameKey : plrGameKeys) {
				GameKey gameKey = plrGameKey.getGameKey();
				switch (gameKey) {
				case UP:
					changeListSelectedIndex(-1);
					break;
				case DOWN:
					changeListSelectedIndex(1);
					break;
				case LEFT:
					changeButton(-1);
					break;
				case RIGHT:
					changeButton(1);
					break;
				case A:
					JButton btn = buttons.get(buttonIndex);
					if (btn != null) {
						btn.doClick();
					}
					break;
				default:
					break;
				}
			}
		}

	}

	private class lstWorlds_SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			buttons.get(buttonIndex).requestFocus();
			gameName = lstWorlds.getSelectedValue();
		}
	}

	private class btnNew_Click implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			createNew();
		}
	}

	private class btnPlay_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			World.load(gameName);
			GUIManager.changeScreen(ScreenType.Play);
		}
	}

	private class btnEdit_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			World.load(gameName);
			GUIManager.changeScreen(ScreenType.Edit);
		}
	}

	private class btnRename_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			File gameFile = getGameFile();
			if (!gameFile.exists()) {
				return;
			}
			tryRename(gameFile);
		}

	}

	private class btnCopy_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			File gameFile = getGameFile();
			if (!gameFile.exists()) {
				return;
			}
			copy(gameFile);
		}
	}

	private class btnDelete_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			File gameFile = getGameFile();
			if (!gameFile.exists()) {
				return;
			}
			if (confirmDelete()) {
				delete(gameFile);
			}
		}
	}

	// =================================================================
	// ACTIONS
	// =================================================================

	private void tryRename(File gameFile) {
		String newName = GUIHelper.getInput("New name:", gameName);
		if (newName.length() > 0) {
			rename(gameFile, newName);
		}
	}

	private void createNew() {
		String worldName = GUIHelper.getInput("Enter a name for your world:",
				"");

		if (!worldName.trim().isEmpty()) {

			String filename = ResManager.getFilename(World.class, worldName);
			File worldFile = new File(filename);

			try {
				if (worldFile.createNewFile()) {
					mdlWorlds.addElement(worldName);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void rename(File gameFile, String newName) {
		try {
			FileHelper.renameKeepExtension(gameFile, newName);
			mdlWorlds.set(lstWorlds.getSelectedIndex(), newName);
			updateGamename(newName);

		} catch (Exception e) {
			// TODO: Show error in GUI
			// TODO: Show a better e.getMessage() than null
			System.out.println("Could not rename '" + gameName + "': "
					+ e.getMessage());
		}
	}

	private void delete(File gameFile) {
		if (gameFile.delete()) {
			mdlWorlds.remove(lstWorlds.getSelectedIndex());
		} else {
			System.out.println("Could not delete '" + gameName + "'");
			// TODO: Show error in GUI
		}
	}

	private boolean confirmDelete() {
		return GUIHelper.confirmDangerous("Are you sure you want to delete "
				+ gameName + "?");
	}

	private void copy(File gameFile) {
		if (FileHelper.copyFileInSameDir(gameFile, " - Copy")) {
			mdlWorlds.addElement(gameName + " - Copy");
		} else {
			System.out.println("Could not create copy of '" + gameName + "'");
			// TODO: Show error in GUI
		}
	}

	void updateGamename(String gameName) {
		this.gameName = gameName;
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

	private File getGameFile() {
		String filename = ResManager.getFilename(World.class, gameName);
		return new File(filename);
	}

}
//