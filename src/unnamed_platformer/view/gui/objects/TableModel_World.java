package unnamed_platformer.view.gui.objects;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import unnamed_platformer.game.other.World;
import unnamed_platformer.res_mgt.ResManager;

public class TableModel_World extends AbstractTableModel {
	private static final long serialVersionUID = -8391825996500804868L;

	protected List<List<Object>> data = new LinkedList<List<Object>>();
	protected List<String> columnNames = new LinkedList<String>();

	public TableModel_World() {
		columnNames.add("World");
		for (String name : ResManager.list(World.class, true)) {
			addRow(new String[] { name });
		}
	}

	public void clear() {
		data.clear();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> clazz = data.get(0).get(columnIndex).getClass();
		return clazz;
	}

	public void addRow(Object[] rowData) {
		List<Object> newRow = new LinkedList<Object>();
		for (Object o : rowData) {
			newRow.add(o);
		}
		addRow(newRow);
	}

	public void addRow(List<Object> newRow) {
		data.add(newRow);
		this.fireTableDataChanged();
	}

	public void removeRow(int row) {
		data.remove(row);
		this.fireTableDataChanged();
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public String getColumnName(int column) {
		Object id = null;

		if (column < columnNames.size() && (column >= 0)) {
			id = columnNames.get(column);
		}
		return (id == null) ? super.getColumnName(column) : id.toString();
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public Object getValueAt(int row, int column) {
		return data.get(row).get(column);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		// This function needs to stay blank
		// Swing is calling it at the wrong time and overwriting the data
	}

	// This is the real setValueAt function
	public void setCellValue(int row, int column, Object value) {
		data.get(row).set(column, value);
		this.fireTableCellUpdated(row, column);
	}

}
