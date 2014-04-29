package app.gui;

import app.App;
import app.App.State;

public class GUI_MainMenu extends GUI_Template {


	public void play() {
		App.state = State.playSelect;
	}
	
	public void edit() {
		App.state = State.editSelect;
	}
}
