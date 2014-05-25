package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_Render extends Screen {

	@Override
	protected void update() {
	}

	@Override
	protected void activate() {
		ViewManager.setRenderCanvasVisibility(true);
		panel.setVisible(false);
	}

}
