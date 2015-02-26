package unnamed_platformer.view.screens;

import unnamed_platformer.game.zones.World;
import unnamed_platformer.view.ViewManager;

public class BaseScreen_Render extends Screen
{

	public BaseScreen_Render() {
		super();

		World.setPlaying(true);
		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(true);
		pnlSurface.setVisible(false);
		pnlSurface.setEnabled(false);
	}

}
