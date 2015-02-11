package unnamed_platformer.view.gui.screens;

import java.awt.Color;
import java.awt.Panel;
import java.util.EnumMap;

import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.view.ViewManager;

public class BaseScreen_Hybrid extends Screen
{
	private static final int INITIAL_TOOLBAR_SIZE = 48;

	EnumMap<Side, Panel> toolbars = new EnumMap<Side, Panel>(Side.class);

	public BaseScreen_Hybrid() {
		super();

		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(true);
		pnlSurface.setLayout(null);

		for (Side side : Side.values()) {
			Panel panel = new Panel(new MigLayout());
			panel.setBackground(StyleRef.COLOR_MAIN);
			panel.setForeground(Color.WHITE);
			toolbars.put(side, panel);

			setToolbarSize(side, INITIAL_TOOLBAR_SIZE);
			pnlSurface.add(panel);
		}
	}

	protected static enum Side {
		left, right, top, bottom
	}

	protected void setToolbarSizes(int left, int right, int top, int bottom) {
		setToolbarSize(Side.left, left);
		setToolbarSize(Side.right, right);
		setToolbarSize(Side.top, top);
		setToolbarSize(Side.bottom, bottom);
	}
	
	protected void setToolbarSize(Side side, int newSize) {
		Panel toolbar = toolbars.get(side);

		int viewWidth = (int) ViewManager.currentResolution.getWidth();
		int viewHeight = (int) ViewManager.currentResolution.getHeight();

		switch (side) {
		case bottom:
			toolbar.setBounds(0, viewHeight - newSize, viewWidth, newSize);
			break;
		case left:
			toolbar.setBounds(0, 0, newSize, viewHeight);
			break;
		case right:
			toolbar.setBounds(viewWidth - newSize, 0, newSize, viewHeight);
			break;
		case top:
			toolbar.setBounds(0, 0, viewWidth, newSize);
			break;
		}

		compensateForToolbars();
	}

	private void compensateForToolbars() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int l = (int) toolbars.get(Side.left).getBounds().getMaxX();
				int r = (int) toolbars.get(Side.right).getBounds().getMinX();
				int t = (int) toolbars.get(Side.top).getBounds().getMaxY();
				int b = (int) toolbars.get(Side.bottom).getBounds().getMinY();

				ViewManager.setRenderCanvasBounds(l, t, r - l, b - t);
			}
		});
	}

}
