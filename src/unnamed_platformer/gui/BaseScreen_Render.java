package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_Render extends ZScreen {

	@Override
	protected void update() {
	}

	public BaseScreen_Render() {
		ViewManager.setRenderCanvasVisibility(true);
		panel.setVisible(false);
	}

}
