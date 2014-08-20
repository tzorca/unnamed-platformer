package unnamed_platformer.gui.objects;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeCellImageRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3121464047823108249L;

	@Override
	public Color getBackgroundNonSelectionColor() {
		return null;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		Object userObject = node.getUserObject();

		if (userObject != null) {
			ImageListEntry imageListEntry = (ImageListEntry) userObject;
			setIcon(imageListEntry.getImage());
			setText(imageListEntry.getDisplayName());
			int height = imageListEntry.getImage().getIconHeight();

			tree.setRowHeight(height > tree.getRowHeight() ? height : tree.getRowHeight());
		}

		return this;
	}
}