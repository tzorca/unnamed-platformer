package unnamed_platformer.view.gui.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

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

import org.lwjgl.input.Keyboard;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.GameKey;
import unnamed_platformer.app.InputManager.PlrGameKey;
import unnamed_platformer.app.KeyCodeTranslator;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.Settings;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;
import unnamed_platformer.view.gui.dialogs.Dialog_KeyAssignment;
import unnamed_platformer.view.gui.objects.ListCellRenderer_PlrGameKey;

import com.google.common.collect.Lists;

public class Screen_Options extends BaseScreen_GUI {

	DefaultListModel<PlrGameKey> mdlKeys = new DefaultListModel<PlrGameKey>();

	// Instantiate GUI components
	JLabel lblTitle = new JLabel("Input Setup");
	JList<PlrGameKey> lstKeys = new JList<PlrGameKey>();
	JPanel pnlButtons = new JPanel();
	JButton btnBack = new JButton("Back");

	List<JButton> buttons = Lists.newArrayList(btnBack);

	// Collect all components
	@SuppressWarnings("unchecked")
	List<? extends JComponent> components = Lists
			.newArrayList(lstKeys, btnBack);

	public Screen_Options() {
		super();	

		// SETUP TITLE
		lblTitle.setFont(StyleRef.FONT_SUB_HEADING);
		lblTitle.setForeground(StyleRef.COLOR_LIGHT_GREY);

		// SETUP KEY LIST
		for (GameKey gk : InputManager.GameKey.values()) {
			mdlKeys.addElement(new PlrGameKey(1, gk));
		}

		lstKeys.setModel(mdlKeys);
		lstKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstKeys.setCellRenderer(new ListCellRenderer_PlrGameKey());
		lstKeys.setFont(StyleRef.FONT_NORMAL);
		lstKeys.setBackground(StyleRef.COLOR_MAIN);
		lstKeys.setForeground(Color.WHITE);
		lstKeys.setSelectionBackground(StyleRef.COLOR_MAIN_HIGHLIGHT);
		lstKeys.addListSelectionListener(new lstKeys_SelectionListener());

		// SETUP LIST SCROLL PANE
		JScrollPane scrlList = new JScrollPane(lstKeys);
		GUIHelper.styleComponentColors(scrlList, StyleRef.COLOR_MAIN_PLUS);
		GUIHelper.styleComponentBorder(scrlList, 0, false);
		scrlList.getViewport().setBackground(StyleRef.COLOR_MAIN);

		// SETUP BUTTONS
		btnBack.addActionListener(new btnSave_Click());

		for (JButton btn : buttons) {
			btn.setFont(StyleRef.FONT_NORMAL);
			btn.setIconTextGap(0);
		}
		GUIHelper.styleButtons(buttons, 6, StyleRef.COLOR_MAIN);

		// ADD GLOBAL LISTENERS FOR ARROW NAVIGATION
		for (JComponent c : components) {
			c.addKeyListener(new Global_KeyListener());
			c.addFocusListener(new component_FocusListener());
		}

		// ADD BUTTONS TO PANEL
		pnlButtons.add(btnBack);

		// SETUP BUTTON PANEL
		pnlButtons.setBackground(StyleRef.COLOR_MAIN_PLUS);

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblTitle, "gapx 8px 8px, pushx, wrap");
		pnlSurface.add(scrlList, "gapx 8px 8px, gapy 16px, grow, pushy, wrap");
		pnlSurface.setBackground(StyleRef.COLOR_MAIN_PLUS);
		pnlSurface.add(pnlButtons, "gapx 8px 8px, gapy 4px");

	}

	private int componentIndex = 1;
	private PlrGameKey currentPlrGameKey = null;

	// ================================================================
	// NAVIGATION
	// ================================================================

	private void changeComponent(int delta) {
		int newIndex = componentIndex + delta;
		if (newIndex >= components.size()) {
			newIndex = 0;
		}
		if (newIndex < 0) {
			newIndex = components.size() - 1;
		}

		componentIndex = newIndex;
		components.get(componentIndex).requestFocusInWindow();
	}

	private void changeListSelectedIndex(int delta) {
		int newIndex = lstKeys.getSelectedIndex() + delta;
		if (newIndex >= mdlKeys.size()) {
			newIndex = 0;
		}
		if (newIndex < 0) {
			newIndex = mdlKeys.size() - 1;
		}
		lstKeys.setSelectedIndex(newIndex);
	}
	
	// =================================================================
	// ACTIONS
	// =================================================================

	private void tryAssignKey(Integer keyCode) {
		if (currentPlrGameKey == null) {
			return;
		}

		Integer jinputKeyCode = KeyCodeTranslator.translateJavaVKToJInputCode(keyCode);
		if (jinputKeyCode == null) {
			System.err.println("Error: No JInput translation found for " + KeyEvent.getKeyText(keyCode));
			return;
		}
		
		String jinputKeyText = Keyboard.getKeyName(jinputKeyCode);
		
		Settings.setString(currentPlrGameKey.toString(), jinputKeyText);
		
		InputManager.loadMappingsFromSettings();
	}

	// =================================================================
	// EVENTS
	// =================================================================

	public void update() {
		super.update();

		// LWJGL JInput Keys
		if (InputManager.keyPressOccurred(GameKey.LEFT, 1)) {
			changeComponent(-1);
		}
		if (InputManager.keyPressOccurred(GameKey.RIGHT, 1)) {
			changeComponent(1);
		}
		if (InputManager.keyPressOccurred(GameKey.UP, 1)) {
			changeListSelectedIndex(-1);
		}
		if (InputManager.keyPressOccurred(GameKey.DOWN, 1)) {
			changeListSelectedIndex(1);
		}
		if (InputManager.keyPressOccurred(GameKey.A, 1)) {
			doComponentAction();
		}

		// Default button selection
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!components.get(componentIndex).hasFocus()) {
					components.get(componentIndex).requestFocusInWindow();
				}
			}
		});
	}

	private void doComponentAction() {
		JComponent component = components.get(componentIndex);
		if (component != null) {
			if (component instanceof JButton) {
				((JButton) component).doClick();
			} else if (component.equals(lstKeys)) {
				showKeyChangeDialog();
			}
		}
	}

	private void showKeyChangeDialog() {
		ParamRunnable assignKeyCallback = new ParamRunnable() {
			public void run(Object param) {
				Integer keyCode = (Integer) param;
				tryAssignKey(keyCode);
			}

		};
		GUIManager.showDialog(new Dialog_KeyAssignment(ViewManager.getFrame(),
				assignKeyCallback));
	}


	// Java VK KeyEvents
	private class Global_KeyListener extends KeyAdapter
	{
		public void keyPressed(KeyEvent e) {
			Collection<PlrGameKey> plrGameKeys = InputManager
					.getGameKeysMatchingKeyEvent(e);

			for (PlrGameKey plrGameKey : plrGameKeys) {
				GameKey gameKey = plrGameKey.getGameKey();
				switch (gameKey) {
				case UP:
					e.consume();
					changeListSelectedIndex(-1);
					break;
				case DOWN:
					e.consume();
					changeListSelectedIndex(1);
					break;
				case LEFT:
					e.consume();
					changeComponent(-1);
					break;
				case RIGHT:
					e.consume();
					changeComponent(1);
					break;
				case A:
					e.consume();
					doComponentAction();
					break;
				case EXIT:
					e.consume();
					Main.doHalt();
					break;
				default:
					break;
				}
			}
		}

	}

	private class lstKeys_SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			components.get(componentIndex).requestFocusInWindow();

			currentPlrGameKey = lstKeys.getSelectedValue();
			JList<?> source = (JList<?>) e.getSource();
			source.ensureIndexIsVisible(lstKeys.getSelectedIndex());
		}

	}

	public class btnSave_Click implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			GUIManager.changeScreen(ScreenType.Title);

		}

	}

	private class component_FocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e) {
			for (int index = 0; index < components.size(); index++) {
				JComponent c = components.get(index);
				if (c.equals(e.getSource())) {
					componentIndex = index;
				}
			}
		}

		public void focusLost(FocusEvent e) {
		}
	}

}
