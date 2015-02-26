package unnamed_platformer.view.gui.hud;

import java.awt.Color;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.StyleGlobals;

public class HUD_Time extends HUD_Component
{
	public HUD_Time() {
		super(new Vector2f(8, 8), StyleGlobals.FONT_HUD, StyleGlobals.COLOR_LIGHT_GREY, Color.BLACK);
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
