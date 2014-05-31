package unnamed_platformer.gui.objects;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import unnamed_platformer.app.App;
import unnamed_platformer.app.App.State;
import unnamed_platformer.app.ContentManager;
import unnamed_platformer.app.FileHelper;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
import unnamed_platformer.gui.GUIHelper;

// TODO: Move file and screen operation logic to a controller
public class WorldCell extends ReadOnlyInteractiveCell implements
		ActionListener {
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

	String gameName;
	WorldTableModel model;

	private int thisRow;

	private int thisColumn;

	public WorldCell(WorldTableModel mdlWorlds) {
		this.model = mdlWorlds;
	}

	// Setup pnlContainer
	{
		pnlContainer.setLayout(new BorderLayout());
		pnlContainer.setOpaque(true);
		pnlContainer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

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

		// TODO: Make JButtons into clickable icon buttons
	}

	private enum Actions {
		PLAY, GOTO_RENAME, EDIT, COPY, DELETE
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Actions.PLAY.name())) {
			GameManager.loadGame(gameName);
			App.state = State.Play;
		} else if (e.getActionCommand().equals(Actions.EDIT.name())) {
			GameManager.loadGame(gameName);
			App.state = State.Edit;
		}

		else {
			String filename = ContentManager.getFilename(ContentType.game,
					gameName);
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
		String newName = GUIHelper.getInput("New name:", gameName);
		if (newName.length() > 0) {
			rename(gameFile, newName);
		}
	}

	private void rename(File gameFile, String newName) {
		try {
			FileHelper.renameKeepExtension(gameFile, newName);
			// Todo: Get the update function working right
			model.setValueAt(newName, thisRow, thisColumn);
		} catch (Exception e) {
			// TODO: Show error in GUI
			App.print("Could not rename " + gameName + ": " + e.getMessage());
		}
	}

	private void delete(File gameFile) {
		if (gameFile.delete()) {
			model.removeRow(gameName);
		} else {
			App.print("Could not delete " + gameName);
			// TODO: Show error in GUI
		}
	}

	private boolean confirmDelete() {
		return GUIHelper.confirmDangerous("Are you sure you want to delete "
				+ gameName + "?");
	}

	private void copy(File gameFile) {
		if (FileHelper.copyFileInSameDir(gameFile, " - Copy")) {
			model.addRow(gameName + " - Copy");
		} else {
			App.print("Could not create copy of " + gameName);
			// TODO: Show error in GUI
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object object,
			boolean isSelected, boolean hasFocus, int row, int column) {
		this.gameName = (String) object;

		lblTitle.setText(gameName);

		if (isSelected || hasFocus) {
			pnlContainer.setBackground(table.getSelectionBackground());
		} else {
			pnlContainer.setBackground(SystemColor.control);
		}
		thisRow = row;
		thisColumn = column;

		// auto update row height to fit
		int height = new Double(pnlContainer.getPreferredSize().getHeight())
				.intValue();
		if (table.getRowHeight(row) < height)
			table.setRowHeight(row, height);

		return pnlContainer;
	}

	@Override
	public Object getCellEditorValue() {
		return gameName;
	}

}