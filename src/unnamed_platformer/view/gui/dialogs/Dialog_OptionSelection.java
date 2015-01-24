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
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.InputManager.PlrGameKey;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.objects.ListCellRenderer_CustomBorder;

/**
 * Saves the string of the selected choice in the callback parameter
 * 
 */
public class Dialog_OptionSelection<T> extends Dialog
{
	private static final long serialVersionUID = -7867279766354367729L;

	private ParamRunnable choiceCallback;
	private T cancelChoice;

	private JPanel pnlMain = new JPanel();
	private JList<T> lstChoices;

	public Dialog_OptionSelection(Frame owner, String message, List<T> choices,
			final T cancelChoice, final ParamRunnable choiceCallback) {
		this(owner, message, choices, cancelChoice, null, choiceCallback);
	}
	
	public Dialog_OptionSelection(Frame owner, String message, List<T> choices,
			final T cancelChoice, final ListCellRenderer<T> listCellRenderer, final ParamRunnable choiceCallback) {
		super(owner);
		this.add(pnlMain);
		this.setUndecorated(true);

		pnlMain.setLayout(new MigLayout());
		pnlMain.setBackground(StyleRef.COLOR_MAIN_PLUS);
		pnlMain.setBorder(BorderFactory
				.createSoftBevelBorder(BevelBorder.RAISED));

		final JLabel lblMessage = new JLabel(message);
		StyleRef.STYLE_MESSAGE.apply(lblMessage);

		pnlMain.add(lblMessage, "wrap, growx, pushx, span");

		this.choiceCallback = choiceCallback;
		this.cancelChoice = cancelChoice;

		DefaultListModel<T> listModel = new DefaultListModel<T>();
		for (T choice : choices) {
			listModel.addElement(choice);
		}

		Border bottomLineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.GRAY);
		Border paddingBorder = new EmptyBorder(4, 8, 4, 8);

		Border paddedBorderWithBottomLine = new CompoundBorder(
				bottomLineBorder, paddingBorder);

		lstChoices = new JList<T>();
		lstChoices.setModel(listModel);
		lstChoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleRef.STYLE_CHOICE_LIST.apply(lstChoices);
		if (listCellRenderer == null) {
		lstChoices.setCellRenderer(new ListCellRenderer_CustomBorder(
				paddedBorderWithBottomLine));
		} else {
			lstChoices.setCellRenderer(listCellRenderer);
		}
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

	private void choose(T choice) {
		choiceCallback.run(choice);
		Dialog_OptionSelection.this.setVisible(false);
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
