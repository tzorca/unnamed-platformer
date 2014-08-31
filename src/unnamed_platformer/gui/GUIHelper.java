package unnamed_platformer.gui;

import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.globals.Ref;

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
				boolean returnValue = JOptionPane.showConfirmDialog(null, msg,
						Ref.APP_TITLE, JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
				paramRunnable.run(returnValue);
			}
		};

		SwingUtilities.invokeLater(dialogRunnable);
		return false;
	}

	public static boolean confirmDangerous(final String msg) {
		boolean returnValue = JOptionPane.showConfirmDialog(null, msg,
				Ref.APP_TITLE, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
		return returnValue;
	}

	// TODO: Change box's title from "Input" to something that makes sense
	public static String getInput(String msg, String defaultVal) {
		String result = JOptionPane.showInputDialog(msg, defaultVal);
		return result != null ? result : "";
	}

	public static void removeButtonPadding(JButton button) {
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));
	}
	
	public static void removeButtonPadding(ArrayList<JButton> buttons) {
		for (JButton button : buttons) {
			removeButtonPadding(button);
		}
	}

	public static void styleButton(JButton button, int paddingSize) {
		Border lineBorder = BorderFactory
				.createLineBorder(GUIManager.GUI_BORDER_COLOR);
	
		Border paddingBorder =new EmptyBorder(paddingSize, (int) (paddingSize*2.5), paddingSize, (int) (paddingSize*2.5));
		
		Border border = BorderFactory.createCompoundBorder(lineBorder,
				paddingBorder);

		button.setBorder(border);
		button.setForeground(GUIManager.GUI_FG_COLOR);
		button.setBackground(GUIManager.GUI_BG_ALT_COLOR);
		button.setContentAreaFilled(false);
		button.setVerticalTextPosition(SwingConstants.CENTER);
		button.setOpaque(true);
	}

	public static void styleButtons(ArrayList<JButton> buttons, int paddingSize) {
		for (JButton button : buttons) {
			styleButton(button, paddingSize);
		}
	}
}
