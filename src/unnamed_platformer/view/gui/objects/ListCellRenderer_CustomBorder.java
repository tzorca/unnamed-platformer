package unnamed_platformer.view.gui.objects;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;

public class ListCellRenderer_CustomBorder extends DefaultListCellRenderer
{
	private static final long serialVersionUID = -8494739790890409146L;

	Border border;
	
	public ListCellRenderer_CustomBorder(Border border) {
		this.border = border;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JComponent c = (JComponent) super.getListCellRendererComponent(list,
				value, index, isSelected, cellHasFocus);

		c.setBorder(border);
		return c;
	}

}