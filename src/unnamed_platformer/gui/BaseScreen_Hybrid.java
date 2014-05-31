package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_Hybrid extends ZScreen {

	@Override
	protected void update() {
	}

	public BaseScreen_Hybrid() {
		ViewManager.setRenderCanvasVisibility(true);
		panel.setVisible(true);
	}

}
