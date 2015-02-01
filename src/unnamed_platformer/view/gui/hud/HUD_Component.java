package unnamed_platformer.view.gui.hud;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.view.gui.objects.SlickLabel;

public abstract class HUD_Component extends SlickLabel
{
	public HUD_Component(Vector2f position) {
		super(position);
	}

	public HUD_Component(Font font, Vector2f position, Color color) {
		super(font, position, color);
	}

	public HUD_Component(Font font, Vector2f position) {
		super(font, position);
	}

	public abstract void update();
}
