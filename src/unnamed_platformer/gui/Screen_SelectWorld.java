package unnamed_platformer.gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import unnamed_platformer.gui.objects.WorldCell;
import unnamed_platformer.gui.objects.WorldTableModel;

// TODO: Implement support for double-clicking in listing
public class Screen_SelectWorld extends BaseScreen_GUI {

	// instance initializer
	{
		JLabel lblGameTitle = new JLabel("Custom Worlds");
		lblGameTitle.setFont(GUIManager.SUB_HEADING_FONT);
		lblGameTitle.setForeground(Color.darkGray);

		WorldTableModel mdlWorlds = new WorldTableModel();
		JTable tblWorlds = new JTable(mdlWorlds);
		tblWorlds.setDefaultRenderer(String.class, new WorldCell(mdlWorlds));
		tblWorlds.setDefaultEditor(String.class, new WorldCell(mdlWorlds));
		tblWorlds.setTableHeader(null);
		tblWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorlds.setRowHeight(64);

		pnlSurface.add(lblGameTitle, GUIManager.CENTER_LAYOUT + ", gapBottom 5%");
		pnlSurface.add(new JScrollPane(tblWorlds), "growx");
	}

}
//