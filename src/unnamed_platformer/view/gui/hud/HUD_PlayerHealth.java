package unnamed_platformer.view.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.entities.PlatformPlayer;
import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;
import unnamed_platformer.view.gui.GUIManager;

public class HUD_PlayerHealth extends HUDComponent
{
	public HUD_PlayerHealth() {
		super(new Vector2f(8,32));
		this.setColor(new Color(16,16,16));
		this.setFont(GUIManager.FONT_HUD);
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

		setText("Energy    " + String.format("% 2d",playerHealth));
	}

}
