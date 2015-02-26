package unnamed_platformer.view.objects;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.InputManager.PlrGameKey;

public class ListCellRenderer_PlrGameKey extends JLabel implements
		ListCellRenderer<PlrGameKey>
{
	private static final long serialVersionUID = 8631524682851129670L;

	public ListCellRenderer_PlrGameKey() {
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.LEFT);
		setHorizontalTextPosition(JLabel.RIGHT);
		setVerticalTextPosition(JLabel.BOTTOM);
		setBorder(new EmptyBorder(2, 8, 2, 0));
	}

	// private static final int MAX_TEXT_LENGTH = 12;

	@Override
	public Component getListCellRendererComponent(
			JList<? extends PlrGameKey> list, PlrGameKey value, int index,
			boolean isSelected, boolean cellHasFocus) {
		String gameKeyStr = value.getGameKey().toString();

		String playerNoStr = String.valueOf(value.getPlayerNo());

		String assignedKeyString;
		try {
			assignedKeyString = InputManager.getAssociatedKey(value).toString();
		} catch (Exception e) {
			assignedKeyString = "Unassigned";
		}

		this.setFont(list.getFont());

		this.setText("P" + playerNoStr + " " + gameKeyStr + " : " + assignedKeyString);

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