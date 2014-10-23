package unnamed_platformer.view.gui.objects;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.lwjgl.input.Keyboard;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.PlrGameKey;
import unnamed_platformer.view.gui.GUIManager;

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

		String assignedKey = Keyboard.getKeyName(InputManager
				.getAssociatedKeyCode(value));

		this.setFont(list.getFont());
		
		this.setText("P" + playerNoStr + " " + gameKeyStr + " : " + assignedKey);

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