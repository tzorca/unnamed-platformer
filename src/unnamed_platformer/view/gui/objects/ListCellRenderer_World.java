package unnamed_platformer.view.gui.objects;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.content_management.ContentManager;
import unnamed_platformer.globals.FileGlobals;

public class ListCellRenderer_World extends JLabel implements
		ListCellRenderer<String>
{
	private static final long serialVersionUID = 8631524682851129670L;

	public ListCellRenderer_World() {
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setIconTextGap(0);
		setBorder(new EmptyBorder(8, 8, 8, 8));
	}

	// private static final Dimension SIZE = new Dimension(192, 192);
	private static final int MAX_TEXT_LENGTH = 32;
	private static final int MIN_TEXT_LENGTH = 32;

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean cellHasFocus) {
		String humanReadableWorldName = new String(value);
		if (humanReadableWorldName.length() > MAX_TEXT_LENGTH) {
			humanReadableWorldName = humanReadableWorldName.substring(0,
					MAX_TEXT_LENGTH - 3) + "...";
		} else if (humanReadableWorldName.length() < MIN_TEXT_LENGTH) {
			StringBuilder sb = new StringBuilder(humanReadableWorldName);
			while (sb.length() < MIN_TEXT_LENGTH) {
				sb.append(" ");
			}
			humanReadableWorldName = sb.toString();
		}

		setFont(list.getFont());
		setText(humanReadableWorldName);

		// include preview image if it exists
		if (ContentManager.contentExists(BufferedImage.class,
				FileGlobals.IMG_PREVIEW_DIR, value)) {
			ImageIcon icon = new ImageIcon(ContentManager.get(
					BufferedImage.class, FileGlobals.IMG_PREVIEW_DIR, value));
			setIcon(icon);
		}

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
