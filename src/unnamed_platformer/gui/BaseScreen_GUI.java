package unnamed_platformer.gui;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.ViewManager;

public class BaseScreen_GUI extends Screen {

	@Override
	protected void update() {
	}

	public BaseScreen_GUI() {
		GameManager.setPlaying(false);
		ViewManager.setRenderCanvasVisibility(false);
		pnlSurface.setVisible(true);
		ViewManager.resetRenderCanvasBounds();
	}


}
