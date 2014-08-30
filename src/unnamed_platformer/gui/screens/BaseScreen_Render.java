package unnamed_platformer.gui.screens;

import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.other.World;

public class BaseScreen_Render extends Screen {

	// Instance initializer
	{
		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(false);
		pnlSurface.setEnabled(false);
		World.setPlaying(true);
	}

}
