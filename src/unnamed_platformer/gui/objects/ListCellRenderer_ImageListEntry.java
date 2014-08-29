package unnamed_platformer.gui.objects;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ListCellRenderer_ImageListEntry extends JLabel implements
		ListCellRenderer<ImageListEntry> {
	private static final long serialVersionUID = 8631524682851129670L;

	public ListCellRenderer_ImageListEntry() {
		setOpaque(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setIconTextGap(4);
		this.setBorder(new EmptyBorder(4, 0, 4, 0));
	}

	private static final int MAX_TEXT_LENGTH = 12;

	@Override
	public Component getListCellRendererComponent(
			JList<? extends ImageListEntry> list, ImageListEntry value,
			int index, boolean isSelected, boolean cellHasFocus) {
		String text = value.getDisplayName();
		if (text.length() > MAX_TEXT_LENGTH) {
			text = text.substring(0, MAX_TEXT_LENGTH - 3) + "...";
		}

		setText(text);
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