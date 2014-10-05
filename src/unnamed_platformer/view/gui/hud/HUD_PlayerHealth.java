package unnamed_platformer.view.gui.hud;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;

public class HUD_PlayerHealth extends HUDComponent
{
	public HUD_PlayerHealth() {
		super(new Vector2f(24, 48));
		this.setColor(Color.lightGray);
		this.setFont(new Font("Tahoma", 0, 24));
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

		setText(String.valueOf(playerHealth));
	}

}
