package unnamed_platformer.gui;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.LevelManager;

public class GUI_Play extends GUI_Template {

	@Override
	public void onStartScreen() {
		if (LevelManager.getLevelCount() == 0 || LevelManager.getCurrentEntities().size() < 3) {
			GameManager.playRandomGame();
		}
	}

}
