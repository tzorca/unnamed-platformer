package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_Render extends Screen {

	@Override
	protected void update() {
	}

	// Instance initializer
	{
		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(false);
		pnlSurface.setEnabled(false);
	}

}
