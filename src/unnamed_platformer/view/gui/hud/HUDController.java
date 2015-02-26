package unnamed_platformer.view.gui.hud;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.zones.World;
import unnamed_platformer.view.ViewManager;

public class HUDController
{
	protected static List<HUD_Component> hudComponents = new ArrayList<HUD_Component>();

	public static void init() {
		hudComponents.add(new HUD_PlayerHealth());
		hudComponents.add(new HUD_Time());
	}
	
	public static void updateAndDraw() {
		if (!World.playing()) {
			return;
		}

		Vector2f cameraPos = ViewManager.getCameraPos();

		for (HUD_Component hudComponent : hudComponents) {
			hudComponent.update();
			hudComponent.draw(cameraPos);
		}
	}

}
