package unnamed_platformer.gui.screens;

import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.other.World;

public class BaseScreen_GUI extends Screen {
	public BaseScreen_GUI() {
		World.setPlaying(false);
		ViewManager.setRenderCanvasVisibility(false);
		pnlSurface.setVisible(true);
		ViewManager.resetRenderCanvasBounds();
	}


}
