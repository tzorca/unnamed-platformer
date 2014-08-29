package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.other.World;

public class BaseScreen_GUI extends Screen {

	@Override
	protected void update() {
	}

	public BaseScreen_GUI() {
		World.setPlaying(false);
		ViewManager.setRenderCanvasVisibility(false);
		pnlSurface.setVisible(true);
		ViewManager.resetRenderCanvasBounds();
	}


}
