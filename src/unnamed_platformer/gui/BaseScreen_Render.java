package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public class BaseScreen_Render extends ZScreen {

	@Override
	protected void update() {
	}

	// Instance initializer
	{
		ViewManager.setRenderCanvasVisibility(true);
		panel.setVisible(false);
		panel.setIgnoreRepaint(true);
		panel.setEnabled(false);
	}

}
