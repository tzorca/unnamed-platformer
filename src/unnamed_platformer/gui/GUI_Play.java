package unnamed_platformer.gui;

import unnamed_platformer.app.GameManager;

public class GUI_Play extends GUI_Template {

	@Override
	public void onStartScreen() {
		if (GameManager.getLevelCount() == 0 || GameManager.getCurrentEntities().size() < 3) {
			GameManager.playRandomGame();
		}
	}

}
