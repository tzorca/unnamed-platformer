package unnamed_platformer.view.gui.objects;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

public class SlickLabel
{
	private Font font;
	private TrueTypeFont ttf;
	private Vector2f position;
	private Color color = Color.black;
	private String text = "";

	private final static Font DEFAULT_FONT = new Font("Verdana", 0, 20);

	public SlickLabel(Font font, Vector2f position, Color color) {
		setFont(font);
		setPosition(position);
		setColor(color);
	}

	public SlickLabel(Font font, Vector2f position) {
		setFont(font);
		setPosition(position);
	}

	public SlickLabel(Vector2f position) {
		setFont(DEFAULT_FONT);
		setPosition(position);
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setFont(Font font) {
		this.font = font;
		ttf = new TrueTypeFont(font, true);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Font getFont() {
		return this.font;
	}

	public Vector2f getPosition() {
		return this.position;
	}

	public Color getColor() {
		return this.color;
	}

	public void draw(Vector2f cameraTopLeft) {
		Vector2f gamePosition = new Vector2f();
		gamePosition.x = cameraTopLeft.x;
		gamePosition.y = cameraTopLeft.y;
		gamePosition.add(position);
		ttf.drawString(gamePosition.x, gamePosition.y, text, color);
	}
}
