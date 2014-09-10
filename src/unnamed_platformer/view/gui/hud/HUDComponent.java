package unnamed_platformer.view.gui.hud;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.view.gui.objects.SlickLabel;

public abstract class HUDComponent extends SlickLabel
{
	public HUDComponent(Vector2f position) {
		super(position);
	}

	public HUDComponent(Font font, Vector2f position, Color color) {
		super(font, position, color);
	}

	public HUDComponent(Font font, Vector2f position) {
		super(font, position);
	}

	public abstract void update();
}
