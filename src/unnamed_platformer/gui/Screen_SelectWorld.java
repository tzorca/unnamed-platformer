package unnamed_platformer.gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import unnamed_platformer.game.parameters.GUIRef;
import unnamed_platformer.gui.objects.WorldCell;
import unnamed_platformer.gui.objects.WorldTableModel;

public class Screen_SelectWorld extends BaseScreen_GUI {

	// instance initializer
	{
		JLabel lblGameTitle = new JLabel("Custom Worlds");
		lblGameTitle.setFont(GUIRef.SUB_HEADING_FONT);
		lblGameTitle.setForeground(Color.darkGray);

		WorldTableModel mdlWorlds = new WorldTableModel();
		JTable tblWorlds = new JTable(mdlWorlds);
		tblWorlds.setDefaultRenderer(String.class, new WorldCell(mdlWorlds));
		tblWorlds.setDefaultEditor(String.class, new WorldCell(mdlWorlds));
		tblWorlds.setTableHeader(null);

		// tblWorlds.setRowSorter
		// GUIHelper.autofitRowHeight(tblWorlds);
		GUIHelper.autofitRowHeight(tblWorlds);
		tblWorlds.setPreferredScrollableViewportSize(tblWorlds
				.getPreferredSize());

		panel.add(lblGameTitle, GUIRef.CENTER_LAYOUT + ", gapBottom 5%");
		panel.add(new JScrollPane(tblWorlds), "growx");
	}

}
//