package unnamed_platformer.gui.objects;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import unnamed_platformer.app.ContentManager;
import unnamed_platformer.game.parameters.ContentRef.ContentType;

// TODO: Improve rendering of update to prevent displaying partial rows
public class WorldTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -8391825996500804868L;
	List<String> data = new ArrayList<String>();

	public WorldTableModel() {
		reloadAll();
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public int getColumnCount() {
		return 1;
	}

	public String getColumnName(int columnIndex) {
		return "";
	}

	public int getRowCount() {
		return (data == null) ? 0 : data.size();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex);
	}

	public void setValueAt(Object val, int row, int column) {
		data.set(row, val.toString());
		this.fireTableCellUpdated(row, column);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void reloadAll() {
		data.clear();
		data.addAll(ContentManager.list(ContentType.game, true));

		fireTableDataChanged();
	}

	public void addRow(String worldName) {
		data.add(worldName);
		fireTableDataChanged();
	}

	// TODO: Make row removal work in model
	public void removeRow(String gameName) {
		data.remove(gameName);
		fireTableDataChanged();

	}

}
