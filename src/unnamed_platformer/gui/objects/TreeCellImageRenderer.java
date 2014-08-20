package unnamed_platformer.gui.objects;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeCellImageRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3121464047823108249L;
	private int heightValue;

	@Override
	public Color getBackgroundNonSelectionColor() {
		return null;
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
			setText(imageListEntry.getDisplayName());
			this.heightValue = imageListEntry.getImage().getIconHeight();

			tree.setRowHeight(0);
		}

		return this;
	}
}