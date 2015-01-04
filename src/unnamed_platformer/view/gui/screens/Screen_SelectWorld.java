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
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import unnamed_platformer.app.FileHelper;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.Settings;
import unnamed_platformer.app.Settings.SettingName;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.InputManager.PlrGameKey;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;
import unnamed_platformer.view.gui.dialogs.Dialog_ChoiceSelection;
import unnamed_platformer.view.gui.objects.ListCellRenderer_World;

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
	List<JButton> buttons = Lists.newArrayList(btnPlay, btnEdit, btnNew,
			btnRename, btnCopy, btnDelete);

	// Collect all components
	@SuppressWarnings("unchecked")
	List<? extends JComponent> components = Lists.newArrayList(lstWorlds,
			btnNew, btnPlay, btnRename, btnEdit, btnCopy, btnDelete);

	private enum WorldSelectState {
		WORLD_SELECT, ACTION_SELECT;
	}

	private int buttonIndex = 0;
	private WorldSelectState state;

	private void changeState(WorldSelectState newState) {
		state = newState;

		if (state == WorldSelectState.ACTION_SELECT) {
			boolean lockedGame = currentGameIsLocked();
			btnEdit.setVisible(lockedGame);
			btnDelete.setVisible(lockedGame);
			btnRename.setVisible(lockedGame);
		}

		pnlButtons.setVisible(state == WorldSelectState.ACTION_SELECT);
	}

	public Screen_SelectWorld() {
		super();

		// SETUP TITLE
		JLabel lblTitle = new JLabel("Select a World");
		StyleRef.STYLE_SUB_HEADING.apply(lblTitle);

		// SETUP WORLD LIST
		Collection<String> worldNames = ResManager.list(World.class, true);

		for (String worldName : worldNames) {
			if (worldName.equals(Settings
					.getString(SettingName.OFFICIAL_LEVELSET_NAME))) {
				mdlWorlds.add(0, worldName);
			} else {
				mdlWorlds.addElement(worldName);
			}
		}
		lstWorlds.setModel(mdlWorlds);
		lstWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleRef.STYLE_WORLD_LIST.apply(lstWorlds);
		lstWorlds.addListSelectionListener(new lstWorlds_SelectionListener());
		lstWorlds.setCellRenderer(new ListCellRenderer_World());
		lstWorlds.setSelectedIndex(0);

		// SETUP WORLD LIST SCROLL PANE
		JScrollPane scrlWorlds = new JScrollPane(lstWorlds);
		StyleRef.STYLE_TYPICAL_SCROLLPANE.apply(scrlWorlds);

		// SETUP BUTTONS
		btnNew.addActionListener(new btnNew_Click());
		btnPlay.addActionListener(new btnPlay_Click());
		btnRename.addActionListener(new btnRename_Click());
		btnEdit.addActionListener(new btnEdit_Click());
		btnCopy.addActionListener(new btnCopy_Click());
		btnDelete.addActionListener(new btnDelete_Click());
		for (JButton btn : buttons) {
			btn.addFocusListener(new btn_FocusListener());
			// btn.setIconTextGap(0);
			StyleRef.STYLE_NORMAL_BUTTON.apply(btn);
		}
		changeState(WorldSelectState.WORLD_SELECT);

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
		pnlButtons.add(Box.createRigidArea(new Dimension(16, 0)));

		// SETUP BUTTON PANEL
		pnlButtons.setBackground(StyleRef.COLOR_MAIN_PLUS);

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblTitle, "gapx 8px 8px, pushx, wrap");
		pnlSurface
				.add(scrlWorlds, "gapx 8px 8px, gapy 16px, grow, pushy, wrap");
		pnlSurface.setBackground(StyleRef.COLOR_MAIN_PLUS);
		pnlSurface.add(pnlButtons, "gapx 8px 8px, gapy 4px");
	}

	private boolean currentGameIsLocked() {
		String gameName = getGameName();
		if (gameName == null) {
			return true;
		}
		return gameName.equals(Settings
				.getString(SettingName.OFFICIAL_LEVELSET_NAME));
	}

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

		if (buttons.get(buttonIndex).isVisible()) {
			buttons.get(buttonIndex).requestFocusInWindow();
		} else {
			changeButton(delta);
		}
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

		buttons.get(buttonIndex).requestFocusInWindow();
	}

	// =================================================================
	// EVENTS
	// =================================================================

	public void update() {
		super.update();

		// LWJGL JInput Keys
		processKeys(InputManager.getPressedGameKeys(), null);

		// Default button selection
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!buttons.get(buttonIndex).hasFocus()) {
					buttons.get(buttonIndex).requestFocusInWindow();
				}
			}
		});
	}

	// Java VK KeyEvents
	private class Global_KeyListener extends KeyAdapter
	{
		public void keyPressed(KeyEvent e) {
			Collection<PlrGameKey> plrGameKeys = InputManager
					.getGameKeysMatchingKeyEvent(e);

			processKeys(plrGameKeys, e);
		}
	}

	private void processKeys(Collection<PlrGameKey> plrGameKeys, KeyEvent e) {

		boolean consume = (e != null);
		for (PlrGameKey plrGameKey : plrGameKeys) {
			GameKey gameKey = plrGameKey.getGameKey();
			switch (gameKey) {
			case LEFT:
				leftPressed();
				break;
			case RIGHT:
				rightPressed();
				break;
			case A:
				aPressed();
				break;
			case B:
				bPressed();
				break;
			case EXIT:
				Main.doHalt();
				break;
			default:
				consume = false;
				break;
			}
		}
		if (consume) {
			e.consume();
		}
	}

	private void leftPressed() {
		switch (state) {
		case ACTION_SELECT:
			changeButton(-1);
			break;
		case WORLD_SELECT:
			changeListSelectedIndex(-1);
			break;
		}
	}

	private void rightPressed() {
		switch (state) {
		case ACTION_SELECT:
			changeButton(1);
			break;
		case WORLD_SELECT:
			changeListSelectedIndex(1);
			break;
		}
	}

	private void aPressed() {
		switch (state) {
		case ACTION_SELECT:
			JButton btn = buttons.get(buttonIndex);
			if (btn != null) {
				btn.doClick();
			}
			break;
		case WORLD_SELECT:
			changeState(WorldSelectState.ACTION_SELECT);
			break;
		}
	}

	private void bPressed() {
		switch (state) {
		case ACTION_SELECT:
			changeState(WorldSelectState.WORLD_SELECT);
			break;
		case WORLD_SELECT:
			GUIManager.changeScreen(ScreenType.Title);
			break;
		}
	}

	private String getGameName() {
		return lstWorlds.getSelectedValue();
	}

	private class lstWorlds_SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			buttons.get(buttonIndex).requestFocusInWindow();
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
			String gameName = getGameName();
			if (gameName == null) {
				return;
			}
			World.load(gameName);
			GUIManager.changeScreen(ScreenType.Play);
		}
	}

	private class btnEdit_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			if (currentGameIsLocked()) {
				return;
			}
			String gameName = getGameName();
			if (gameName == null) {
				return;
			}

			World.load(gameName);
			GUIManager.changeScreen(ScreenType.Edit);
		}
	}

	private class btnRename_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			if (currentGameIsLocked()) {
				return;
			}

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
			if (currentGameIsLocked()) {
				return;
			}

			final File gameFile = getGameFile();
			if (!gameFile.exists()) {
				return;
			}

			String gameName = getGameName();
			if (gameName == null) {
				return;
			}

			// Confirm delete
			String message = "Really delete `" + gameName + "`?";
			final String cancelText = "Cancel";
			final List<String> choices = Lists.newArrayList("Delete",
					cancelText);
			final ParamRunnable afterChoice = new ParamRunnable() {
				public void run(Object param) {
					final String choice = (String) param;

					if (choice.equals(cancelText)) {
						return;
					}
					delete(gameFile);
				}
			};
			Dialog_ChoiceSelection dlgConfirmDelete = new Dialog_ChoiceSelection(
					ViewManager.getFrame(), message, choices, cancelText,
					afterChoice);
			dlgConfirmDelete.setVisible(true);
		}
	}

	private class btnTitle_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			GUIManager.changeScreen(ScreenType.Title);
		}
	}

	// =================================================================
	// ACTIONS
	// =================================================================

	private void tryRename(File gameFile) {
		String gameName = getGameName();
		if (gameName == null) {
			return;
		}

		String newName = GUIHelper.getInput("New name:", gameName);
		if (newName.length() > 0) {
			rename(gameFile, newName);
		}
	}

	private void createNew() {
		String worldName = GUIHelper.getInput("Enter a name for your world:",
				"New World");

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

		} catch (Exception e) {
			String gameName = getGameName();
			if (gameName == null) {
				gameName = "(null)";
			}

			// TODO: Show error in GUI
			System.out.println("Could not rename '" + gameName + "': "
					+ e.getMessage());
		}
	}

	private void delete(File gameFile) {
		if (gameFile.delete()) {
			mdlWorlds.remove(lstWorlds.getSelectedIndex());
		} else {
			String gameName = getGameName();
			if (gameName == null) {
				gameName = "(null)";
			}

			System.out.println("Could not delete '" + gameName + "'");
			// TODO: Show error in GUI
		}
	}

	private void copy(File gameFile) {
		String gameName = getGameName();
		if (gameName == null) {
			gameName = "(null)";
		}

		if (FileHelper.copyFileInSameDir(gameFile, " - Copy")) {
			mdlWorlds.addElement(gameName + " - Copy");
		} else {
			System.out.println("Could not create copy of '" + gameName + "'");
			// TODO: Show error in GUI
		}
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
		String gameName = getGameName();
		if (gameName == null) {
			gameName = "Unknown";
		}

		String filename = ResManager.getFilename(World.class, gameName);
		return new File(filename);
	}

}
//