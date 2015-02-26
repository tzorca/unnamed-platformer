package unnamed_platformer.view.gui.hud;

import java.awt.Color;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.StyleGlobals;

public class HUD_PlayerHealth extends HUD_Component
{
	public HUD_PlayerHealth() {
		super(new Vector2f(8, 32), StyleGlobals.FONT_HUD, StyleGlobals.COLOR_LIGHT_GREY, Color.BLACK);
	}

	@Override
	public void update() {
		Level currentLevel = World.getCurrentLevel();

		if (currentLevel == null) {
			return;
		}

		PlatformPlayer player = (PlatformPlayer) currentLevel.getPlayer();

		if (player == null) {
			setText("");
			return;
		}

		int playerHealth = player.getHealth();

		String playerHealthString = String.valueOf(playerHealth);
		if (playerHealthString.length() < 2) {
			playerHealthString = " " + playerHealthString;
		}

		setText("Energy    " + playerHealthString);
	}

}
