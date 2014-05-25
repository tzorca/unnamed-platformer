package unnamed_platformer.gui;

import unnamed_platformer.app.ViewManager;

public abstract class BaseScreen_Hybrid extends Screen {
	private static final long serialVersionUID = 7812847012465705191L;

	@Override
	protected void update() {
	}
	
	@Override
	protected void activate() {

		ViewManager.setRenderCanvasVisibility(true);

		panel.setVisible(true);
	}

}
