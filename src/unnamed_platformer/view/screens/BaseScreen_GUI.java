package unnamed_platformer.view.screens;

import unnamed_platformer.game.zones.World;
import unnamed_platformer.view.ViewManager;

public class BaseScreen_GUI extends Screen
{
	public BaseScreen_GUI() {
		super();
		
		World.setPlaying(false);
//		ViewManager.resetRenderCanvasBounds();
		ViewManager.setRenderCanvasVisibility(false);
		pnlSurface.setVisible(true);
		ViewManager.validate();
	}

}
