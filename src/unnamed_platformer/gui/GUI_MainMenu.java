package unnamed_platformer.gui;

import unnamed_platformer.app.App;
import unnamed_platformer.app.App.State;

public class GUI_MainMenu extends GUI_Template {


	public void play() {
		App.state = State.playSelect;
	}
	
	public void edit() {
		App.state = State.editSelect;
	}
}
