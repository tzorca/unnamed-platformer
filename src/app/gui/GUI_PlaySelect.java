package app.gui;

import app.App;
import app.App.State;
import app.ContentManager;
import app.ContentManager.ContentType;
import app.GameManager;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

public class GUI_PlaySelect extends GUI_Template {

	ListBox<String> lstGames;

	@Override
	public void onStartScreen() {
		hookControls();
		lstGames.clear();

		String[] nameList = ContentManager.list(ContentType.game, true);
		for (String name : nameList) {
			lstGames.addItem(name);
		}
		selectGameIndex(0);
	}

	private void selectGameIndex(int i) {
		lstGames.setFocusItemByIndex(i);
		lstGames.selectItemByIndex(i);
		currentGameName = lstGames.getFocusItem();
	}


	@SuppressWarnings("unchecked")
	private void hookControls() {
		lstGames = GUIManager.findElement("lstGames").getNiftyControl(
				ListBox.class);
	}

	@NiftyEventSubscriber(id = "lstGames")
	public void lstGames_SelectionChanged(final String id,
			final ListBoxSelectionChangedEvent<String> event) {
		currentGameName = lstGames.getFocusItem();
	}

	String currentGameName = "";

	@NiftyEventSubscriber(id = "btnPlaySelected")
	public void btnPlaySelected_Clicked(final String id, final ButtonClickedEvent event) {
		if (currentGameName.length() == 0) {
			return;
		}
		GameManager.loadGame(currentGameName);
		App.delayedStateChange(State.play);
	}
	
	@NiftyEventSubscriber(id = "btnPlayRandom")
	public void btnPlayRandom_Clicked(final String id, final ButtonClickedEvent event) {
		GameManager.generateRandomGame();
		App.delayedStateChange(State.play);
	}

}
