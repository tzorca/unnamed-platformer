package unnamed_platformer.view.gui.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import unnamed_platformer.game.other.World;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.objects.TableCell_World;
import unnamed_platformer.view.gui.objects.TableModel_World;

public class Screen_SelectWorld extends BaseScreen_GUI
{

	TableModel_World mdlWorlds = new TableModel_World();

	public Screen_SelectWorld() {
		super();

		JLabel lblGameTitle = new JLabel("Custom Worlds");
		lblGameTitle.setFont(GUIManager.SUB_HEADING_FONT);
		lblGameTitle.setForeground(GUIManager.COLOR_WHITE);

		JButton btnNewWorld = new JButton("Create World");
		GUIHelper.styleButton(btnNewWorld, 6);
		btnNewWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String worldName = GUIHelper.getInput(
						"Enter a name for your world:", "");

				if (!worldName.trim().isEmpty()) {

					String filename = ResManager.getFilename(World.class,
							worldName);
					File worldFile = new File(filename);

					try {
						if (worldFile.createNewFile()) {
							mdlWorlds.addRow(new String[] {
								worldName
							});
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		JTable tblWorlds = new JTable(mdlWorlds);
		tblWorlds.setDefaultRenderer(String.class, new TableCell_World(
				mdlWorlds));
		tblWorlds
				.setDefaultEditor(String.class, new TableCell_World(mdlWorlds));
		tblWorlds.setTableHeader(null);
		tblWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorlds.setRowHeight(64);
		tblWorlds.setBackground(GUIManager.COLOR_DARK_BLUE_2);
		tblWorlds.setForeground(GUIManager.COLOR_WHITE);

		JScrollPane scrlTable = new JScrollPane(tblWorlds);
		GUIHelper.styleComponentColors(scrlTable, false);
		GUIHelper.styleBorder(scrlTable, 0, false);
		scrlTable.getViewport().setBackground(GUIManager.COLOR_DARK_BLUE_2);

		pnlSurface.add(lblGameTitle, GUIManager.CENTER_LAYOUT);
		pnlSurface.add(btnNewWorld, "wrap, right");
		pnlSurface.add(scrlTable, "gapy 8px, grow, pushy");
		pnlSurface.setBackground(GUIManager.COLOR_DARK_BLUE_3);

	}

}
//