package app.gui;

import game.parameters.ContentRef.ContentType;

import java.util.Collection;

import app.App;
import app.App.State;
import app.ContentManager;
import app.GameManager;
import de.lessvoid.nifty.controls.ListBox;

public class GUI_PlaySelect extends GUI_Template {

	ListBox<String> lstGames;

	@Override
	public void onStartScreen() {
		hookControls();
		lstGames.clear();

		Collection<String> nameList = ContentManager.list(ContentType.game,
				true);
		for (String name : nameList) {
			lstGames.addItem(name);
		}
		selectGameIndex(0);
	}

	private void selectGameIndex(int i) {
		lstGames.setFocusItemByIndex(i);
		lstGames.selectItemByIndex(i);
	}

	@SuppressWarnings("unchecked")
	private void hookControls() {
		lstGames = GUIManager.findElement("lstGames").getNiftyControl(
				ListBox.class);
	}

	public void btnPlaySelected_Clicked() {
		String currentGameName = lstGames.getFocusItem();
		if (currentGameName.length() == 0) {
			return;
		}
		GameManager.loadGame(currentGameName);
		App.delayedStateChange(State.play);
	}

	public void btnPlayRandom_Clicked() {
		GameManager.generateRandomGame();
		App.delayedStateChange(State.play);
	}

}
