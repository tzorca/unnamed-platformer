package app.gui;

import model.Game;
import model.logic.StringHelper;
import app.App;
import app.App.State;
import app.ContentManager;
import app.ContentManager.ContentType;
import app.GameManager;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;

public class GUI_EditSelect extends GUI_Template {

	ListBox<String> lstGames;
	TextField txtName;

	@Override
	public void onStartScreen() {
		hookControls();
		lstGames.clear();

		String[] nameList = ContentManager.list(ContentType.game, true);
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

		txtName = GUIManager.findElement("txtName").getNiftyControl(
				TextField.class);
	}

	public void btnNew_Clicked() {

		if (!StringHelper.isValidFilename(txtName.getRealText())) {
			System.out.println("Invalid filename " + txtName.getRealText());
			return;
		}

		Game newGame = new Game(txtName.getRealText(), true);
		newGame.setCurrentLevel(0);

		if (newGame.save(txtName.getRealText())) {
			lstGames.addItem(txtName.getRealText());
			txtName.setText("");
			selectGameByIndex(lstGames.getItems().size() - 1);
		} else {
			System.out.println("Could not create game with name of "
					+ txtName.getRealText());
		}
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
