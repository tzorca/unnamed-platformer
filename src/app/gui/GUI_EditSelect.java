package app.gui;

import java.util.Collection;

import model.Game;
import model.logic.StringHelper;
import model.parameters.ContentRef.ContentType;
import model.structures.Callback;
import app.App;
import app.App.State;
import app.ContentManager;
import app.GameManager;
import de.lessvoid.nifty.controls.ListBox;

public class GUI_EditSelect extends GUI_Template {

	ListBox<String> lstGames;

	@Override
	public void onStartScreen() {
		hookControls();
		lstGames.clear();

		Collection<String> nameList = ContentManager.list(ContentType.game, true);
		for (String name : nameList) {
			if (name.startsWith(".")) {
				continue;
			}
			lstGames.addItem(name);
		}
		selectGameByIndex(0);
	}

	private void selectGameByIndex(int i) {
		lstGames.setFocusItemByIndex(i);
		lstGames.selectItemByIndex(i);
	}

	@SuppressWarnings("unchecked")
	private void hookControls() {
		lstGames = GUIManager.findElement("lstGames").getNiftyControl(
				ListBox.class);
	}

	private void createNewGame(String newGameName) {
		if (newGameName == null || newGameName.trim().length() == 0) {
			return;
		}

		if (!StringHelper.isValidFilename(newGameName.trim())) {
			System.out.println(newGameName + " is not a valid filename");
			return;
		}

		Game newGame = new Game(newGameName, true);
		newGame.setCurrentLevel(0);

		if (newGame.save(newGameName)) {
			lstGames.addItem(newGameName);
			selectGameByIndex(lstGames.getItems().size() - 1);
		} else {
			System.out.println("Could not create game with name of "
					+ newGameName);
		}
	}

	public void btnNew_Clicked() {

		GUI_Popups.showInputBox("Create a new game called:", new Callback() {
			public void execute(Object newGameName) {
				createNewGame((String) newGameName);
			}
		});
	}

	public void btnOpen_Clicked() {

		String currentGameName = lstGames.getFocusItem();
		if (currentGameName.length() == 0) {
			return;
		}

		GameManager.loadGame(currentGameName);
		App.delayedStateChange(State.edit);
	}
}
