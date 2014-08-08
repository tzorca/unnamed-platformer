package unnamed_platformer.gui;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import unnamed_platformer.globals.Ref;
import unnamed_platformer.structures.ParamRunnable;

public class GUIHelper {
	public static void autofitRowHeight(JTable table) {
		int maxRowHeight = 0;
		try {
			for (int row = 0; row < table.getRowCount(); row++) {
				int rowHeight = table.getRowHeight();

				for (int column = 0; column < table.getColumnCount(); column++) {
					Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
					maxRowHeight = Math.max(rowHeight, maxRowHeight);
				}

				table.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}
	}

	public static boolean confirmDangerousWithCallback(final String msg, final ParamRunnable paramRunnable) {
		Runnable dialogRunnable = new Runnable() {
			public void run() {
				boolean returnValue = JOptionPane.showConfirmDialog(null, msg, Ref.APP_TITLE,
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
				paramRunnable.run(returnValue);
			}
		};

		SwingUtilities.invokeLater(dialogRunnable);
		return false;
	}

	public static boolean confirmDangerous(final String msg) {
		boolean returnValue = JOptionPane.showConfirmDialog(null, msg, Ref.APP_TITLE, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
		return returnValue;
	}

	// TODO: Change box's title from "Input" to something that makes sense
	public static String getInput(String msg, String defaultVal) {
		String result = JOptionPane.showInputDialog(msg, defaultVal);
		return result != null ? result : "";
	}

	public static void removeButtonPadding(JButton btn) {
		btn.setBorder(null);
		btn.setBorderPainted(false);
		btn.setMargin(new Insets(0, 0, 0, 0));
	}
}
