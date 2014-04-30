// Tim Zorca
// CPSC 3520
package model.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import model.Ref.Flag;
import model.Ref.SizeMethod;
import model.structures.FlColor;
import model.structures.FlagMap;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import app.ContentManager;
import app.ContentManager.ContentType;

public class Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2898448772127546782L;
	protected FlagMap flags = new FlagMap();
	protected Rectangle box = new Rectangle();
	FlColor color = null;
	String textureName = "";

	transient Texture texture = null;
	protected Point startPos = new Point();

	public Texture getTexture() {
		if (texture == null && textureName != null) {
			setTexture(textureName);
		}
		
		return texture;
	}

	public void setTexture(String textureName) {
		this.texture = (Texture) ContentManager.get(ContentType.texture,
				textureName);
		this.textureName = textureName;
	}

	public int getX() {
		return box.x;
	}

	public int getY() {
		return box.y;
	}

	public Rectangle getBox() {
		return box;
	}

	public void setRect(Rectangle box) {
		this.box = box;
	}

	public FlColor getColor() {
		return color;
	}

	public void setColor(FlColor color) {
		this.color = color;
	}

	public Entity(String textureName, Point pos, FlagMap flags) {
		setupEntity(textureName, new Rectangle(pos.x, pos.y, 0, 0),
				SizeMethod.TEXTURE, flags);
		this.startPos = getPos();
	}

	public Entity() {
		setupEntity(null, new Rectangle(), SizeMethod.ABSOLUTE, new FlagMap());
		this.startPos = getPos();
	}

	public Entity(String textureName, Point pos, int width, FlagMap flags) {
		setupEntity(textureName, new Rectangle(pos.x, pos.y, width, width),
				SizeMethod.TEXTURE_SCALE, flags);
		this.startPos = getPos();

	}

	private void setupEntity(String textureName, Rectangle r,
			SizeMethod sizeMethod, FlagMap flags) {
		setTexture(textureName);
		this.box.x = r.x;
		this.box.y = r.y;

		switch (sizeMethod) {
		case ABSOLUTE:
			this.box.width = r.width;
			this.box.height = r.height;
			break;
		case TEXTURE:
			this.box.width = texture.getImageWidth();
			this.box.height = texture.getImageHeight();
			break;
		case TEXTURE_SCALE:
			this.box.width = r.width;
			this.box.height = (int) (texture.getImageHeight() * (r.width / (texture
					.getImageWidth() + 0.0f)));
			break;
		default:
			break;
		}

		this.flags = flags;

		this.startPos = getPos();
	}

	public void update(float delta) {
	}

	public boolean collidesWith(Entity otherEntity) {
		return box.intersects(otherEntity.box);

	}

	public boolean pointInside(Point p) {
		return box.contains(p);
	}

	public Point getPos() {
		return new Point(box.x, box.y);
	}

	public Vector2f getPosVector2f() {
		return new Vector2f(box.x, box.y);
	}

	public void setPos(Point newPoint) {
		box.x = newPoint.x;
		box.y = newPoint.y;
	}

	public Point getCenter() {
		return new Point(box.x + box.width / 2, box.y + box.height / 2);
	}

	public void setCenter(Point p) {
		box.x = p.x - box.width / 2;
		box.y = p.y - box.height / 2;
	}

	public boolean checkFlag(Flag f) {
		return flags.containsKey(f) && flags.get(f);
	}

	public void setFlag(Flag f, boolean b) {
		flags.put(f, b);
	}

	public void toggleFlag(Flag f) {
		if (!flags.containsKey(f)) {
			flags.put(f, true);
		} else {
			flags.put(f, !flags.get(f));
		}
	}

	public void setY(int y) {
		this.box.y = y;
	}

	public void setX(int x) {
		this.box.x = x;

	}

	public String getTextureName() {
		return this.textureName;
	}

	public boolean hasNoFlags() {
		return flags.isEmpty();
	}

	public boolean isDynamic() {
		return !this.flags.isEmpty();
	}

	public void setStartPos(Point pos) {
		this.startPos = pos;
	}

}
