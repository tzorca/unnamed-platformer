package unnamed_platformer.view.gui.hud;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.ShadowEffect;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.view.ViewManager;

public abstract class HUD_Component
{
	private UnicodeFont font;
	private Vector2f position;
	private String text = "";

	public HUD_Component(Vector2f position, Font font, Color fontColor, Color outlineColor) {
		setFont(buildUnicodeFont(font, fontColor, outlineColor));
		setPosition(position);
	}
	
	public HUD_Component(Vector2f position, UnicodeFont font) {
		setFont(font);
		setPosition(position);
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setFont(UnicodeFont font) {
		this.font = font;
	}

	/**
	 * @param font
	 *            A Font object
	 * @param fontColor
	 *            The color of the font
	 * @param outlineColor
	 *            Null for no outline
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static UnicodeFont buildUnicodeFont(Font font, Color fontColor,
			Color shadowColor) {
		UnicodeFont uFont = new UnicodeFont(font);
		uFont.addAsciiGlyphs();
		if (shadowColor != null) {
			uFont.getEffects().add(new ShadowEffect(shadowColor, 1, 1, 0.5f));
		}
		uFont.getEffects().add(new ColorEffect(fontColor));
		try {
			uFont.loadGlyphs();
			return uFont;
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void loadGlyphs() {
		try {
			this.font.loadGlyphs();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public UnicodeFont getFont() {
		return this.font;
	}

	public Vector2f getPosition() {
		return this.position;
	}

	public void draw(Vector2f cameraTopLeft) {
		Vector2f gamePosition = new Vector2f();
		gamePosition.x = cameraTopLeft.x;
		gamePosition.y = cameraTopLeft.y;
		gamePosition.add(position);
		
		ViewManager.saveState();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		font.drawString(gamePosition.x, gamePosition.y, text);
		ViewManager.loadState();
	}

	public abstract void update();
}
