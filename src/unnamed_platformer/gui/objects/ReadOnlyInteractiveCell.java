package unnamed_platformer.gui.objects;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public abstract class ReadOnlyInteractiveCell extends AbstractCellEditor
		implements TableCellEditor, TableCellRenderer {
	private static final long serialVersionUID = 4749665181832067296L;

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object optionsCellObject, boolean isSelected, int row, int column) {
		return getTableCellRendererComponent(table, optionsCellObject, isSelected, true, row, column);
	}


}
