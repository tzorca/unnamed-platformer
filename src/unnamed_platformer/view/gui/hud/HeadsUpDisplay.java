package unnamed_platformer.view.gui.hud;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.other.World;
import unnamed_platformer.view.ViewManager;

public class HeadsUpDisplay
{
	protected static List<HUDComponent> hudComponents = new ArrayList<HUDComponent>();

	static {
		hudComponents.add(new HUD_PlayerHealth());
	}

	public static void updateAndDraw() {
		if (!World.playing()) {
			return;
		}

		Vector2f cameraPos = ViewManager.getCameraPos();

		for (HUDComponent hudComponent : hudComponents) {
			hudComponent.update();
			hudComponent.draw(cameraPos);
		}
	}

}
