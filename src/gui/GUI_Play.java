package gui;

import app.GameManager;
import app.LevelManager;

public class GUI_Play extends GUI_Template {

	@Override
	public void onStartScreen() {
		if (LevelManager.getLevelCount() == 0 || LevelManager.getCurrentEntities().size() < 3) {
			GameManager.generateRandomGame();
		}
	}

}
