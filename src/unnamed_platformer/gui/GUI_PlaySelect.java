package unnamed_platformer.gui;

import java.util.Collection;

import unnamed_platformer.app.App;
import unnamed_platformer.app.ContentManager;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.App.State;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
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
		App.state = State.Play;
	}

	public void btnPlayRandom_Clicked() {
		GameManager.generateRandomGame();
		App.state = State.Play;
	}

}
