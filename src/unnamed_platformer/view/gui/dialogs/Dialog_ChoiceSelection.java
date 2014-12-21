package unnamed_platformer.view.gui.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.PlrGameKey;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.objects.ListCellRenderer_CustomBorder;

/**
 * Saves the string of the selected choice in the callback parameter
 * 
 */
public class Dialog_ChoiceSelection extends Dialog
{
	private static final long serialVersionUID = -7867279766354367729L;

	private ParamRunnable choiceCallback;
	private String cancelChoice;

	private JPanel pnlMain = new JPanel();
	private JList<String> lstChoices;

	public Dialog_ChoiceSelection(Frame owner, String message,
			List<String> choices, final String cancelChoice,
			final ParamRunnable choiceCallback) {
		super(owner);
		this.add(pnlMain);
		this.setUndecorated(true);
		
		pnlMain.setLayout(new MigLayout());
		pnlMain.setBackground(StyleRef.COLOR_MAIN_PLUS);
		pnlMain.setBorder(BorderFactory
				.createSoftBevelBorder(BevelBorder.RAISED));

		final JLabel lblMessage = new JLabel(message);
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setFont(StyleRef.FONT_NORMAL);
		lblMessage.setBorder(new EmptyBorder(0, 0, 0, 0));
		pnlMain.add(lblMessage, "wrap, growx, pushx, span");
		pnlMain.add(new JSeparator(), "wrap, growx, span");

		this.choiceCallback = choiceCallback;
		this.cancelChoice = cancelChoice;

		DefaultListModel<String> mdlChoices = new DefaultListModel<String>();
		for (String choice : choices) {
			mdlChoices.addElement(choice);
		}

		Border bottomLineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.GRAY);
		Border paddingBorder = new EmptyBorder(4, 8, 4, 8);

		Border paddedBorderWithBottomLine = new CompoundBorder(
				bottomLineBorder, paddingBorder);

		lstChoices = new JList<String>();
		lstChoices.setModel(mdlChoices);
		lstChoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstChoices.setFont(StyleRef.FONT_SMALL);
		lstChoices.setBackground(StyleRef.COLOR_MAIN_PLUS);
		lstChoices.setForeground(Color.WHITE);
		lstChoices.setSelectionBackground(StyleRef.COLOR_MAIN_HIGHLIGHT);
		lstChoices.setCellRenderer(new ListCellRenderer_CustomBorder(
				paddedBorderWithBottomLine));
		lstChoices.addListSelectionListener(new lstChoices_SelectionListener());
		lstChoices.addKeyListener(new Global_KeyListener());
		lstChoices.setSelectedIndex(0);
		pnlMain.add(lstChoices, "wrap, growx, span");

		this.addKeyListener(new Global_KeyListener());

		this.pack();
		this.setLocationRelativeTo(owner);
	}

	private class Global_KeyListener extends KeyAdapter
	{
		public void keyPressed(KeyEvent e) {

			Collection<PlrGameKey> gameKeys = InputManager
					.getGameKeysMatchingKeyEvent(e);

			// No matching keys
			if (gameKeys.size() == 0) {
				return;
			}

			boolean consumeEvent = true;
			for (PlrGameKey gk : gameKeys) {
				switch (gk.getGameKey()) {
				case A:
					choose(lstChoices.getSelectedIndex());
					break;
				case B:
					choose(cancelChoice);
					break;
				case UP:
					moveSelection(-1);
					break;
				case DOWN:
					moveSelection(1);
					break;
				default:
					consumeEvent = false;
					break;
				}
			}

			if (consumeEvent) {
				e.consume();
			}
		}
	}

	private void choose(int choiceIndex) {
		choose(lstChoices.getModel().getElementAt(choiceIndex));
	}

	private void choose(String choice) {
		choiceCallback.run(choice);
		Dialog_ChoiceSelection.this.setVisible(false);
		ViewManager.focusRenderCanvas();
	}

	private void moveSelection(int delta) {
		int newIndex = lstChoices.getSelectedIndex() + delta;
		int maxIndex = lstChoices.getModel().getSize();

		if (newIndex >= maxIndex) {
			newIndex = 0;
		} else if (newIndex < 0) {
			newIndex = maxIndex - 1;
		}

		lstChoices.setSelectedIndex(newIndex);
	}

	private class lstChoices_SelectionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) {

			JList<?> source = (JList<?>) e.getSource();
			source.ensureIndexIsVisible(source.getSelectedIndex());
		}

	}

}
