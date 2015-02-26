package unnamed_platformer.view.gui.screens;

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
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import unnamed_platformer.app.Main;
import unnamed_platformer.app.Settings;
import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.InputManager.PlrGameKey;
import unnamed_platformer.input.RawKey;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;
import unnamed_platformer.view.gui.dialogs.Dialog_KeyAssignment;
import unnamed_platformer.view.gui.objects.ListCellRenderer_PlrGameKey;

import com.google.common.collect.Lists;

public class Screen_Options extends BaseScreen_GUI
{

	DefaultListModel<PlrGameKey> mdlKeys = new DefaultListModel<PlrGameKey>();

	// Instantiate GUI components
	JLabel lblTitle = new JLabel("Input Setup");
	JList<PlrGameKey> lstKeys = new JList<PlrGameKey>();

	// Collect all components
	@SuppressWarnings("unchecked")
	List<? extends JComponent> components = Lists.newArrayList(lstKeys);

	public Screen_Options() {
		super();

		// SETUP TITLE
		StyleGlobals.STYLE_SUB_HEADING.apply(lblTitle);

		// SETUP KEY LIST
		for (GameKey gk : GameKey.values()) {
			mdlKeys.addElement(new PlrGameKey(1, gk));
		}

		lstKeys.setModel(mdlKeys);
		lstKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstKeys.setCellRenderer(new ListCellRenderer_PlrGameKey());
		lstKeys.addListSelectionListener(new lstKeys_SelectionListener());
		lstKeys.setSelectedIndex(0);
		StyleGlobals.STYLE_TYPICAL_LIST.apply(lstKeys);

		// SETUP LIST SCROLL PANE
		JScrollPane scrlList = new JScrollPane(lstKeys);
		StyleGlobals.STYLE_TYPICAL_SCROLLPANE.apply(scrlList);

		// ADD GLOBAL LISTENERS FOR ARROW NAVIGATION
		for (JComponent c : components) {
			c.addKeyListener(new Global_KeyListener());
			c.addFocusListener(new component_FocusListener());
		}

		// ADD COMPONENTS TO MAIN PANEL
		pnlSurface.add(lblTitle, "gapx 8px 8px, pushx, wrap");
		pnlSurface.add(scrlList, "gapx 8px 8px, gapy 16px, grow, pushy, wrap");
		pnlSurface.setBackground(StyleGlobals.COLOR_MAIN_PLUS);
	}

	private int componentIndex = 0;
	private PlrGameKey currentPlrGameKey = null;

	// ================================================================
	// NAVIGATION
	// ================================================================

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

	private void tryAssignKey(RawKey key) {
		if (currentPlrGameKey == null) {
			return;
		}

		Settings.setString(currentPlrGameKey.toString(), key.toString());

		InputManager.loadMappingsFromSettings();

		// prevent new key from activating immediately
		InputManager.disableNextPress(currentPlrGameKey);

		Main.setHotKeysAllowed(true);
	}

	// =================================================================
	// EVENTS
	// =================================================================

	public void update() {
		super.update();

		processKeys(InputManager.getPressedGameKeys(), null);

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
				RawKey key = (RawKey) param;
				tryAssignKey(key);
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

			processKeys(plrGameKeys, e);
		}
	}

	private void processKeys(Collection<PlrGameKey> plrGameKeys, KeyEvent e) {
		boolean consume = (e != null);
		for (PlrGameKey plrGameKey : plrGameKeys) {
			GameKey gameKey = plrGameKey.getGameKey();
			switch (gameKey) {
			case UP:
				changeListSelectedIndex(-1);
				break;
			case DOWN:
				changeListSelectedIndex(1);
				break;
			case A:
				doComponentAction();
				break;
			case B:
				GUIManager.changeScreen(ScreenType.Title);
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
