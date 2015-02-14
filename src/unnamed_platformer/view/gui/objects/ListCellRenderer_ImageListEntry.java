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

	int lineWidth = 128;
	
	public ListCellRenderer_ImageListEntry(int lineWidth) {
		this.lineWidth = lineWidth;
		init();
	}
	public ListCellRenderer_ImageListEntry() {
		init();
	}
		
	private void init() {
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setIconTextGap(4);
		setBorder(new EmptyBorder(4, 4, 4, 4));
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends ImageListEntry> list, ImageListEntry value,
			int index, boolean isSelected, boolean cellHasFocus) {

		String displayText = String.format(
				"<html><div style=\"width:%dpx;\">%s</div><html>", lineWidth,
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