package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public abstract class BaseScreen_GUI extends Screen {

	@Override
	protected void update() {
	}

	@Override
	protected void activate() {
		ViewManager.setRenderCanvasVisibility(false);
		panel.setVisible(true);
	}

}
