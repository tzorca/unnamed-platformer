package game.entities;

import game.parameters.Ref.Flag;
import game.parameters.Ref.SizeMethod;
import game.structures.FlagMap;
import game.structures.Graphic;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Entity implements Serializable {
	private static final long serialVersionUID = 2898448772127546782L;

	// protected Behaviour behaviour;
	protected FlagMap flags = new FlagMap();
	protected Rectangle box = new Rectangle();
	public Graphic graphic;

	protected Point startPos = new Point();

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

	public Entity(Graphic graphic, Point pos, FlagMap flags) {
		setupEntity(graphic, new Rectangle(pos.x, pos.y, 0, 0),
				SizeMethod.TEXTURE, flags);
		this.startPos = getPos();
	}

	public Entity() {
		setupEntity(null, new Rectangle(), SizeMethod.ABSOLUTE, new FlagMap());
		this.startPos = getPos();
	}

	public Entity(Graphic graphic, Point pos, int width, FlagMap flags) {
		setupEntity(graphic, new Rectangle(pos.x, pos.y, width, width),
				SizeMethod.TEXTURE_SCALE, flags);
		this.startPos = getPos();

	}

	private void setupEntity(Graphic graphic, Rectangle r,
			SizeMethod sizeMethod, FlagMap flags) {
		this.graphic = graphic;
		this.box.x = r.x;
		this.box.y = r.y;

		Texture texture = graphic.getTexture();

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

	public void update(long delta) {
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

	public boolean hasNoFlags() {
		return flags.isEmpty();
	}

	public void setStartPos(Point pos) {
		this.startPos = pos;
	}

}
