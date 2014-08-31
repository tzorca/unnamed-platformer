package unnamed_platformer.gui.screens;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.objects.TableCell_World;
import unnamed_platformer.gui.objects.TableModel_World;

// TODO: Implement support for double-clicking in listing
public class Screen_SelectWorld extends BaseScreen_GUI {

	// instance initializer
	{
		JLabel lblGameTitle = new JLabel("Custom Worlds");
		lblGameTitle.setFont(GUIManager.SUB_HEADING_FONT);
		lblGameTitle.setForeground(GUIManager.GUI_FG_COLOR);

		TableModel_World mdlWorlds = new TableModel_World();
		JTable tblWorlds = new JTable(mdlWorlds);
		tblWorlds.setDefaultRenderer(String.class, new TableCell_World(mdlWorlds));
		tblWorlds.setDefaultEditor(String.class, new TableCell_World(mdlWorlds));
		tblWorlds.setTableHeader(null);
		tblWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorlds.setRowHeight(64);
		tblWorlds.setBackground(GUIManager.GUI_BG_COLOR);
		tblWorlds.setForeground(GUIManager.GUI_FG_COLOR);
		
		JScrollPane scrlTable = new JScrollPane(tblWorlds);
		scrlTable.getViewport().setBackground(GUIManager.GUI_BORDER_COLOR);
		
		pnlSurface.add(lblGameTitle, GUIManager.CENTER_LAYOUT + ", gapBottom 5%");
		pnlSurface.add(scrlTable, "growx");
		pnlSurface.setBackground(GUIManager.GUI_BORDER_COLOR);
		
	}

}
//