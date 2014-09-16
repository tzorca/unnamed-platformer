package unnamed_platformer.view.gui.objects;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import unnamed_platformer.app.FileHelper;
import unnamed_platformer.game.other.World;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.gui.GUIHelper;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;

import com.google.common.collect.Lists;

// TODO: Show screenshots of levels
// TODO: Move file and screen operation logic to a controller
public class TableCell_World extends TableCell_ReadOnlyInteractive implements ActionListener {
	private static final long serialVersionUID = 4749665181832067296L;

	// Instantiate GUI components
	JPanel pnlContainer = new JPanel();
	JLabel lblTitle = new JLabel();
	JPanel pnlButtons = new JPanel();
	JButton btnPlay = new JButton("Play");
	JButton btnRename = new JButton("Rename");
	JButton btnEdit = new JButton("Edit");
	JButton btnCopy = new JButton("Copy");
	JButton btnDelete = new JButton("Delete");

	private String gameName;
	TableModel_World model;

	private int thisRow;

	private int thisColumn;

	public TableCell_World(TableModel_World mdlWorlds) {
		this.model = mdlWorlds;
	}

	// Setup pnlContainer
	{
		pnlContainer.setLayout(new BorderLayout());
		pnlContainer.setOpaque(true);
		pnlContainer.setBorder(BorderFactory.createEmptyBorder(10, 16, 6, 8));

		btnPlay.setActionCommand(Actions.PLAY.name());
		btnPlay.addActionListener(this);

		btnRename.setActionCommand(Actions.GOTO_RENAME.name());
		btnRename.addActionListener(this);

		btnEdit.setActionCommand(Actions.EDIT.name());
		btnEdit.addActionListener(this);

		btnCopy.setActionCommand(Actions.COPY.name());
		btnCopy.addActionListener(this);

		btnDelete.setActionCommand(Actions.DELETE.name());
		btnDelete.addActionListener(this);

		pnlButtons.setOpaque(false);

		pnlContainer.add(lblTitle, BorderLayout.WEST);
		pnlContainer.add(pnlButtons, BorderLayout.EAST);
		pnlButtons.add(btnPlay);
		pnlButtons.add(btnRename);
		pnlButtons.add(btnEdit);
		pnlButtons.add(btnCopy);
		pnlButtons.add(btnDelete);
		
		lblTitle.setForeground(GUIManager.COLOR_WHITE);
		GUIHelper.styleButtons(Lists.newArrayList(btnPlay, btnRename, btnEdit, btnCopy, btnDelete), 8);

		// TODO: Make JButtons into clickable icon buttons
	}

	private enum Actions {
		PLAY, GOTO_RENAME, EDIT, COPY, DELETE
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Actions.PLAY.name())) {
			World.load(getGameName());
			GUIManager.changeScreen(ScreenType.Play);
		} else if (e.getActionCommand().equals(Actions.EDIT.name())) {
			World.load(getGameName());
			GUIManager.changeScreen(ScreenType.Edit);
		}

		else {
			String filename = ResManager.getFilename(World.class, getGameName());
			File gameFile = new File(filename);

			if (!gameFile.exists()) {
				return;
			}

			if (e.getActionCommand().equals(Actions.GOTO_RENAME.name())) {
				tryRename(gameFile);
			} else if (e.getActionCommand().equals(Actions.COPY.name())) {
				copy(gameFile);
			} else if (e.getActionCommand().equals(Actions.DELETE.name())) {
				if (confirmDelete()) {
					delete(gameFile);
				}
			}

		}
	}

	private void tryRename(File gameFile) {
		String newName = GUIHelper.getInput("New name:", getGameName());
		if (newName.length() > 0) {
			rename(gameFile, newName);
		}
	}

	private void rename(File gameFile, String newName) {
		try {
			FileHelper.renameKeepExtension(gameFile, newName);
			model.setCellValue(thisRow, thisColumn, newName);
			update(newName);

		} catch (Exception e) {
			// TODO: Show error in GUI
			// TODO: Show a better e.getMessage() than null
			System.out.println("Could not rename '" + getGameName() + "': " + e.getMessage());
		}
	}

	private void delete(File gameFile) {
		if (gameFile.delete()) {
			model.removeRow(thisRow);
		} else {
			System.out.println("Could not delete '" + getGameName() + "'");
			// TODO: Show error in GUI
		}
	}

	private boolean confirmDelete() {
		return GUIHelper.confirmDangerous("Are you sure you want to delete " + getGameName() + "?");
	}

	private void copy(File gameFile) {
		if (FileHelper.copyFileInSameDir(gameFile, " - Copy")) {
			model.addRow(new String[] { getGameName() + " - Copy" });
		} else {
			System.out.println("Could not create copy of '" + getGameName() + "'");
			// TODO: Show error in GUI
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus,
			int row, int column) {
		update((String) object);

		if (isSelected || hasFocus) {
			pnlContainer.setBackground(table.getSelectionBackground());
		} else {
			pnlContainer.setBackground(table.getBackground());
		}
		thisRow = row;
		thisColumn = column;

		return pnlContainer;
	}

	@Override
	public Object getCellEditorValue() {
		return getGameName();
	}

	String getGameName() {
		return gameName;
	}

	void update(String gameName) {
		this.gameName = gameName;
		lblTitle.setText(gameName);
	}

}