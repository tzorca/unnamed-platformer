package unnamed_platformer.game.behaviours;

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;
import unnamed_platformer.view.gui.screens.Screen_Edit;

public class Inter_LevelExit extends Interaction
{
	int relativeDestination;

	public Inter_LevelExit(Entity source, int relativeDestination) {
		super(source);
		this.relativeDestination = relativeDestination;
	}

	@Override
	public boolean performInteraction(Entity target) {
		// TODO: Show current level in HUD
		// TODO: Create GameFinished screen and ask if user wants to restart
		
		if (GUIManager.atScreen(ScreenType.Edit)) {
			Screen_Edit currentScreen = ((Screen_Edit)GUIManager.getScreen());
			
			currentScreen.toggleEditMode();
			
		} else {
			World.setPlaying(false);
			World.setLevelByIndex(World.getCurrentLevelIndex()
					+ relativeDestination);
			GUIManager.changeScreen(ScreenType.Transition);
		}

		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
