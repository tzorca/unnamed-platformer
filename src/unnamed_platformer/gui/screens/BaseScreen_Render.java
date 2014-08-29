package unnamed_platformer.gui.screens;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_Render extends Screen {

	// Instance initializer
	{
		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(false);
		pnlSurface.setEnabled(false);
	}

}
