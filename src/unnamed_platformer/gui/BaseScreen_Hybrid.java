package unnamed_platformer.gui;

import java.awt.Panel;
import java.util.EnumMap;

import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.globals.ViewRef;

public class BaseScreen_Hybrid extends Screen {

	@Override
	protected void update() {
	}

	private static final int INITIAL_TOOLBAR_SIZE = 48;

	EnumMap<Side, Panel> toolbars = new EnumMap<Side, Panel>(Side.class);

	// instance initializer
	{
		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(true);
		pnlSurface.setLayout(null);

		for (Side side : Side.values()) {
			Panel panel = new Panel(new MigLayout());
			panel.setBackground(ViewRef.GUI_BG_COLOR);
			panel.setForeground(ViewRef.GUI_FG_COLOR);
			toolbars.put(side, panel);

			setToolbarSize(side, INITIAL_TOOLBAR_SIZE);
			pnlSurface.add(panel);
		}
	}

	protected static enum Side {
		left, right, top, bottom
	}

	protected void setToolbarSize(Side side, int newSize) {
		Panel toolbar = toolbars.get(side);

		int viewWidth = (int) ViewRef.DEFAULT_RESOLUTION.getWidth();
		int viewHeight = (int) ViewRef.DEFAULT_RESOLUTION.getHeight();
		

		switch (side) {
		case bottom:
			toolbar.setBounds(0, viewHeight - newSize, viewWidth,
					newSize);
			break;
		case left:
			toolbar.setBounds(0, 0, newSize, viewHeight);
			break;
		case right:
			toolbar.setBounds(viewWidth - newSize, 0, newSize,
					viewHeight);
			break;
		case top:
			toolbar.setBounds(0, 0, viewWidth, newSize);
			break;
		}

		compensateForToolbars();
	}

	private void compensateForToolbars() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int left = (int) toolbars.get(Side.left).getBounds().getMaxX();
				int right = (int) toolbars.get(Side.right).getBounds()
						.getMinX();
				int top = (int) toolbars.get(Side.top).getBounds().getMaxY();
				int bottom = (int) toolbars.get(Side.bottom).getBounds()
						.getMinY();

				ViewManager.setRenderCanvasBounds(left, top, right - left,
						bottom - top);

			}
		});
	}

}
