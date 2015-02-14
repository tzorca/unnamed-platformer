package unnamed_platformer.view.gui.objects;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ListCellRenderer_ImageListEntry extends JLabel implements
		ListCellRenderer<ImageListEntry>
{
	private static final long serialVersionUID = 8631524682851129670L;

	public ListCellRenderer_ImageListEntry() {
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setIconTextGap(4);
		setBorder(new EmptyBorder(4, 4, 4, 4));
	}

	private static final int LINE_WIDTH = 12;

	@Override
	public Component getListCellRendererComponent(
			JList<? extends ImageListEntry> list, ImageListEntry value,
			int index, boolean isSelected, boolean cellHasFocus) {

		String displayText = String.format(
				"<html><div style=\"width:%dem;\">%s</div><html>", LINE_WIDTH,
				value.getDisplayName());

		setFont(list.getFont());
		setText(displayText);
		setIcon(value.getImage());

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
}