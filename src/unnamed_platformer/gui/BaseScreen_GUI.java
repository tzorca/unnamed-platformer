package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_GUI extends ZScreen {

	@Override
	protected void update() {
	}

	public BaseScreen_GUI() {
		ViewManager.setRenderCanvasVisibility(false);
		panel.setVisible(true);
	}

}
