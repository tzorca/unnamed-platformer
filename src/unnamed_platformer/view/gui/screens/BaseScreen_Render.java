package unnamed_platformer.view.gui.screens;

import unnamed_platformer.game.other.World;
import unnamed_platformer.view.ViewManager;

public class BaseScreen_Render extends Screen {

	public BaseScreen_Render() {
		super();
		
		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(false);
		pnlSurface.setEnabled(false);
		World.setPlaying(true);
	}

}
