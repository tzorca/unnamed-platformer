package unnamed_platformer.gui;

import java.util.Collection;

import unnamed_platformer.app.App;
import unnamed_platformer.app.ContentManager;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.App.State;
import unnamed_platformer.game.Game;
import unnamed_platformer.game.logic.StringHelper;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
import unnamed_platformer.game.structures.Callback;
import de.lessvoid.nifty.controls.ListBox;

public class GUI_EditSelect extends GUI_Template {

	ListBox<String> lstGames;

	@Override
	public void onStartScreen() {
		hookControls();
		lstGames.clear();

		Collection<String> nameList = ContentManager.list(ContentType.game,
				true);
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
			App.print(newGameName + " is not a valid filename");
			return;
		}

		Game newGame = new Game(newGameName, true);
		newGame.setCurrentLevel(0);

		if (newGame.save(newGameName)) {
			lstGames.addItem(newGameName);
			selectGameByIndex(lstGames.getItems().size() - 1);
		} else {
			App.print("Could not create game with name of "
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
		App.state = State.Edit;
	}
}
