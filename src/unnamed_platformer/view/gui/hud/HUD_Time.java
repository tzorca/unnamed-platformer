package unnamed_platformer.view.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;
import unnamed_platformer.view.gui.GUIManager;

public class HUD_Time extends HUDComponent
{
	public HUD_Time() {
		super(new Vector2f(8, 8));
		this.setColor(new Color(16, 16, 16));
		this.setFont(GUIManager.FONT_HUD);
	}

	@Override
	public void update() {
		Level currentLevel = World.getCurrentLevel();

		if (currentLevel == null) {
			return;
		}

		int totalSeconds = currentLevel.getElapsedSeconds();

		int minutes = totalSeconds / 60;
		int seconds = totalSeconds - minutes * 60;

		setText("Time   " + String.format("%02d", minutes) + ":"
				+ String.format("%02d", seconds));
	}

}
