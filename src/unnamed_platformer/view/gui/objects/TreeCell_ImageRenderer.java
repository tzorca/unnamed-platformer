package unnamed_platformer.view.gui.objects;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.screens.Screen_Edit;

public class TreeCell_ImageRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3121464047823108249L;
	private int heightValue;

	@Override
	public Color getBackgroundNonSelectionColor() {
		return null;
	}

	@Override
	public Color getTextSelectionColor() {
		return GUIManager.COLOR_WHITE;
	}

	@Override
	public Color getTextNonSelectionColor() {
		return GUIManager.COLOR_WHITE;
	}

	@Override
	public int getHeight() {
		return heightValue;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		Object userObject = node.getUserObject();

		if (userObject != null) {
			ImageListEntry imageListEntry = (ImageListEntry) userObject;
			setIcon(imageListEntry.getImage());
			setText(" " + imageListEntry.getDisplayName());
			
//			int iconHeight = imageListEntry.getImage().getIconHeight();
			this.heightValue = Screen_Edit.ROW_HEIGHT ;
			tree.setRowHeight(Screen_Edit.ROW_HEIGHT);
			
			// add padding on left
			this.setBorder(new EmptyBorder(0,8,0,0));
		}

		return this;
	}
}