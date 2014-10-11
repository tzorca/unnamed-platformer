package unnamed_platformer.view.gui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.InputManager.PlrGameKey;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.view.ViewManager;

public final class GUIHelper
{

	public interface ParamRunnable
	{
		public void run(Object param);
	}

	public static void autofitRowHeight(JTable table) {
		int maxRowHeight = 0;
		try {
			for (int row = 0; row < table.getRowCount(); row++) {
				int rowHeight = table.getRowHeight();

				for (int column = 0; column < table.getColumnCount(); column++) {
					Component comp = table.prepareRenderer(
							table.getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
					maxRowHeight = Math.max(rowHeight, maxRowHeight);
				}

				table.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}
	}

	public static boolean confirmDangerousWithCallback(final String msg,
			final ParamRunnable paramRunnable) {
		Runnable dialogRunnable = new Runnable() {
			public void run() {
				boolean returnValue = confirmDangerous(msg);
				paramRunnable.run(returnValue);
			}
		};

		SwingUtilities.invokeLater(dialogRunnable);
		return false;
	}

	public static boolean confirmDangerous(final String msg) {
		Object[] options = {
				"Yes", "No"
		};
		boolean returnValue = JOptionPane.showOptionDialog(ViewManager.getFrame(), msg,
				Ref.APP_TITLE, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, options, options[1]) == 0;
		return returnValue;
	}

	// TODO: Change box's title from "Input" to something that makes sense
	public static String getInput(String msg, String defaultVal) {
		String result = JOptionPane.showInputDialog(null, msg, defaultVal);
		return result != null ? result : "";
	}

	public static void removeButtonPadding(JButton button) {
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));
	}

	public static void removeButtonPadding(List<JButton> buttons) {
		for (JButton button : buttons) {
			removeButtonPadding(button);
		}
	}

	public static void styleButton(final JButton button, final int paddingSize) {
		styleBorder(button, paddingSize, true);
		styleComponentColors(button, true);
		button.setContentAreaFilled(false);
		button.setVerticalTextPosition(SwingConstants.CENTER);
		button.setOpaque(true);
	}

	public static void styleComponentColors(final JComponent component,
			boolean focusHighlight) {
		component.setForeground(GUIManager.COLOR_WHITE);
		component.setBackground(GUIManager.COLOR_DARK_BLUE_2);

		if (focusHighlight) {

			component.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					component.setBackground(GUIManager.COLOR_HIGHLIGHT_BLUE);
				}

				@Override
				public void focusLost(FocusEvent e) {
					component.setBackground(GUIManager.COLOR_DARK_BLUE_2);
				}

			});
		}
	}

	public static void styleBorder(JComponent component, int paddingSize,
			boolean clickable) {
		Border raisedBevelBorder = BorderFactory
				.createSoftBevelBorder(BevelBorder.RAISED);
		Border loweredBevelBorder = BorderFactory
				.createSoftBevelBorder(BevelBorder.LOWERED);

		Border paddingBorder = new EmptyBorder(paddingSize,
				(int) (paddingSize * 2.5), paddingSize,
				(int) (paddingSize * 2.5));

		final Border compoundLoweredBorder = BorderFactory
				.createCompoundBorder(loweredBevelBorder, paddingBorder);

		final Border compoundRaisedBorder = BorderFactory.createCompoundBorder(
				raisedBevelBorder, paddingBorder);

		if (clickable) {
			component.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					JComponent component = (JComponent) e.getSource();
					component.setBorder(compoundLoweredBorder);
				}

				public void mouseReleased(MouseEvent e) {
					JComponent component = (JComponent) e.getSource();
					component.setBorder(compoundRaisedBorder);
				}
			});
		}

		component.setBorder(clickable ? compoundRaisedBorder
				: compoundLoweredBorder);
	}

	public static void styleButtons(final List<JButton> buttons,
			final int paddingSize) {
		for (JButton button : buttons) {
			styleButton(button, paddingSize);
		}
	}

	public static void addDirectionalNavigation(final List<JButton> buttons,
			final Runnable exitRunnable) {
		for (int i = 0; i < buttons.size(); i++) {

			// Get current button
			final JButton currentButton = buttons.get(i);

			// Get previous and next buttons
			int prevIndex = i - 1;
			if (prevIndex < 0) {
				prevIndex = buttons.size() - 1;
			}
			final JButton prevButton = buttons.get(prevIndex);
			int nextIndex = i + 1;
			if (nextIndex >= buttons.size()) {
				nextIndex = 0;
			}
			final JButton nextButton = buttons.get(nextIndex);

			// Add directional navigation
			currentButton.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {

					Collection<PlrGameKey> gameKeys = InputManager
							.getGameKeysMatchingKeyEvent(e);

					// No matching keys
					if (gameKeys.size() == 0) {
						return;
					}

					for (PlrGameKey gk : gameKeys) {
						switch (gk.getGameKey()) {
						case A:
							currentButton.doClick();
							break;
						case B:
							if (exitRunnable != null) {
								exitRunnable.run();
							}
							break;
						case LEFT:
							prevButton.requestFocus();
							break;
						case RIGHT:
							nextButton.requestFocus();
							break;
						case EXIT:
							Main.doHalt();
						default:
							break;
						}
					}
				}
			});
		}
	}

}
