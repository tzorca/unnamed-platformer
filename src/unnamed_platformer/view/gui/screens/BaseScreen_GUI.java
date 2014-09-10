package unnamed_platformer.view.gui.screens;

import unnamed_platformer.game.other.World;
import unnamed_platformer.view.ViewManager;

public class BaseScreen_GUI extends Screen
{
	public BaseScreen_GUI() {
		super();
		
		World.setPlaying(false);
		ViewManager.setRenderCanvasVisibility(false);
		pnlSurface.setVisible(true);
		ViewManager.resetRenderCanvasBounds();
	}

}
